private Node performArithmeticOp(int opType, Node left, Node right) {
  if (opType == Token.ADD && (NodeUtil.mayBeString(left, false) || NodeUtil.mayBeString(right, false))) {
    return null;
  }

  double result;

  Double lValObj = NodeUtil.getNumberValue(left);
  if (lValObj == null) {
    // Return null if the left operand is not a number
    return null;
  }
  Double rValObj = NodeUtil.getNumberValue(right);
  if (rValObj == null) {
    // Return null if the right operand is not a number
    return null;
  }

  double lval = lValObj;
  double rval = rValObj;

  switch (opType) {
    case Token.BITAND:
      result = ScriptRuntime.toInt32(lval) & ScriptRuntime.toInt32(rval);
      break;
    case Token.BITOR:
      result = ScriptRuntime.toInt32(lval) | ScriptRuntime.toInt32(rval);
      break;
    case Token.BITXOR:
      result = ScriptRuntime.toInt32(lval) ^ ScriptRuntime.toInt32(rval);
      break;
    case Token.ADD:
      result = lval + rval;
      break;
    case Token.SUB:
      result = lval - rval;
      break;
    case Token.MUL:
      result = lval * rval;
      break;
    case Token.MOD:
      if (rval == 0) {
        // Throw an error when dividing by zero
        throw new ArithmeticException("Divide by zero");
      }
      result = lval % rval;
      break;
    case Token.DIV:
      if (rval == 0) {
        // Throw an error when dividing by zero
        throw new ArithmeticException("Divide by zero");
      }
      result = lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  if (String.valueOf(result).length() <= String.valueOf(lval).length() + String.valueOf(rval).length() + 1 &&
      Math.abs(result) <= MAX_FOLD_NUMBER) {
    Node newNumber = Node.newNumber(result);
    return newNumber;
  } else if (Double.isNaN(result)) {
    return Node.newString(Token.NAME, "NaN");
  } else if (result == Double.POSITIVE_INFINITY) {
    return Node.newString(Token.NAME, "Infinity");
  } else if (result == Double.NEGATIVE_INFINITY) {
    return new Node(Token.NEG, Node.newString(Token.NAME, "Infinity"));
  }

  return null;
}