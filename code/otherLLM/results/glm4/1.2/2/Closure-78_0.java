private Node performArithmeticOp(int opType, Node left, Node right) {
  // Unlike other operations, ADD operands are not always converted
  // to Number.
  if (opType == Token.ADD &&
      (NodeUtil.mayBeString(left, false) ||
       NodeUtil.mayBeString(right, false))) {
    return null;
  }

  double result;

  // TODO(johnlenz): Handle NaN with unknown value. BIT ops convert NaN
  // to zero so this is a little awkward here.

  Double lValObj = NodeUtil.getNumberValue(left);
  if (lValObj == null) {
    return null;
  }
  Double rValObj = NodeUtil.getNumberValue(right);
  if (rValObj == null) {
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
        error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
        throw new ArithmeticException("Division by zero");
      }
      result = lval % rval;
      break;
    case Token.DIV:
      if (rval == 0) {
        error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
        throw new ArithmeticException("Division by zero");
      }
      result = lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  // TODO(johnlenz): consider removing the result length check.
  // length of the left and right value plus 1 byte for the operator.
  if (String.valueOf(result).length() <=
      String.valueOf(lval).length() + String.valueOf(rval).length() + 1 &&
      
      // Additional checks for folding expressions could go here...

      true) { // Placeholder condition, should be replaced with actual logic
    return optimizeSubtree(Node.newNode(result)); // Example node creation
  }

  return Node.newNode(result); // Return a new node with the result value
}