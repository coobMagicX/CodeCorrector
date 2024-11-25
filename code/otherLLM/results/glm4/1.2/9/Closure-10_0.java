static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

// Adjustments made to mayBeStringHelper:
static boolean mayBeStringHelper(Node n) {
  switch (n.getType()) {
    case Token.FUNCTION:
      return containsFunction(n);

    case Token.STRING:
      return true;

    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
    case Token.NULL:
      // These node types are not strings, so we can return false directly.
      return false;

    case Token.BOOL:
    case Token.OBJECT:
    case Token.ARRAY:
    case Token.NEG:
    case Token.NOT:
    case Token.NAME:
      // Nodes of these types might evaluate to a string value.
      // Call getNumberValue to handle the conversion and check for strings.
      Double value = getNumberValue(n);
      if (value != null) {
        return isNumericString(value.toString());
      }
      break;

    case Token.BINARY:
      BinaryNode bn = (BinaryNode) n;
      TernaryValue left = getPureBooleanValue(bn.getLeftChild());
      TernaryValue right = getPureBooleanValue(bn.getRightChild());

      if (left == TernaryValue.UNKNOWN || right == TernaryValue.UNKNOWN) {
        // If either operand is unknown, this is not a definitive string.
        return false;
      }

      boolean leftIsString = left.toBoolean(true);
      boolean rightIsString = right.toBoolean(true);

      // Both sides must be strings or one side must be a number that can be converted to a string.
      if ((leftIsString && bn.getOperator() == Token.PLUS) || // Only '+' operator allows for string conversion
          (rightIsString && bn.getOperator() == Token.PLUS)) {
        return true;
      }
      break;

    case Token.CALL:
      // A function call might return a string, check if it can.
      Node firstChild = n.getFirstChild();
      if (firstChild != null) {
        String functionName = firstChild.getString();
        if (functionName.equals("toString")) {
          return true; // toString is always returning a string
        }
      }
      break;

    case Token.THIS:
    case Token.SUPER:
    case Token.IDENTIFIER:
    case Token.LITERAL_REGEXP:
    case Token.ERROR:
    default:
      // All other types are not strings.
      return false;
  }

  // If we reach here, it's not clear whether this node is a string or not.
  return false;
}

// Helper method to determine if a number can be represented as a string
static boolean isNumericString(String value) {
  try {
    Double.parseDouble(value); // This will throw an exception for non-numeric strings
    return true;
  } catch (NumberFormatException e) {
    return false;
  }
}