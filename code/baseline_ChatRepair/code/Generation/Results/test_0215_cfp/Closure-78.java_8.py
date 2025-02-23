private Node performArithmeticOp(int opType, Node left, Node right) {
  // For string concatenation in ADD
  if (opType == Token.ADD && (NodeUtil.mayBeString(left, false) || NodeUtil.mayBeString(right, false))) {
    return null;  // Relevant handling or string concatenation logic
  }

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
      return Node.newNumber(ScriptRuntime.toInt32(lval) & ScriptRuntime.toInt32(rval));
    case Token.BITOR:
      return Node.newNumber(ScriptRuntime.toInt32(lval) | ScriptRuntime.toInt32(rval));
    case Token.BITXOR:
      return Node.newNumber(ScriptRuntime.toInt32(lval) ^ ScriptRuntime.toInt32(rval));
    case Token.ADD:
      return Node.newNumber(lval + rval);
    case Token.SUB:
      return Node.newNumber(lval - rval);
    case Token.MUL:
      return Node.newNumber(lval * rval);
    case Token.MOD:
    case Token.DIV:
      if (rval == 0) {
        // Throw a specific runtime exception to handle division by zero
        throw new ArithmeticException("Division by zero");
      }
      return Node.newNumber(opType == Token.MOD ? lval % rval : lval / rval);
    default:
      throw new Error("Unexpected arithmetic operator");
  }
}
