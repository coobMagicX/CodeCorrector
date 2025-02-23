private Node performArithmeticOp(int opType, Node left, Node right) {
  // Unlike other operations, ADD operands are not always converted
  // to Number.
  if (opType == Token.ADD && (NodeUtil.mayBeString(left, false) || NodeUtil.mayBeString(right, false))) {
    return null;
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

  double result;
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
        return Node.newNumber(Double.NaN);
      }
      result = lval % rval;
      break;
    case Token.DIV:
      if (rval == 0) {
        if (lval == 0) {
          return Node.newNumber(Double.NaN);
        }
        return Node.newNumber(lval > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
      }
      result = lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  return Node.newNumber(result);
}
