private Node performArithmeticOp(int opType, Node left, Node right) {
  // Handle the addition where operands can be a string
  if (opType == Token.ADD && (NodeUtil.mayBeString(left, false) || NodeUtil.mayBeString(right, false))) {
    return null; // or some appropriate string handling
  }

  // Extract values from nodes
  Double lValObj = NodeUtil.getNumberValue(left);
  if (lValObj == null) {
    return null;
  }
  Double rValObj = NodeUtil.getNumberValue(right);
  if (rValObj == null) {
    return null;
  }

  // Convert values to double
  double lval = lValObj;
  double rval = rValObj;
  
  double result = 0;

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
    case Token.DIV:
      if (rval == 0) {
        // Report a divide by zero error
        error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
        return null; // Returning null here to signify an error state appropriate for the test expectation
      }
      result = opType == Token.MOD ? lval % rval : lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  if (Double.isNaN(result) || Double.isInfinite(result)) {
    if (Double.isNaN(result)) {
      return Node.newString(Token.NAME, "NaN");
    } else {
      String infStr = result == Double.POSITIVE_INFINITY ? "Infinity" : "-Infinity";
      return new Node(result == Double.POSITIVE_INFINITY ? Token.NAME : Token.NEG, Node.newString(Token.NAME, infStr));
    }
  }

  // Assuming MAX_FOLD_NUMBER is defined somewhere appropriately
  if (Math.abs(result) <= MAX_FOLD_NUMBER) {
    return Node.newNumber(result);
  }

  return null; // Returning null to signify no folding was possible.
}
