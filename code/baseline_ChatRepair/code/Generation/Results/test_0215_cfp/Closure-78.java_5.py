private Node performArithmeticOp(int opType, Node left, Node right) {
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
        return Node.newString("NaN");
      }
      result = lval % rval;
      break;
    case Token.DIV:
      if (rval == 0) {
        if (lval == 0) {
          return Node.newString("NaN");
        } else {
          return lval > 0 ? Node.newString("Infinity") : new Node(Token.NEG, Node.newString("Infinity"));
        }
      }
      result = lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  if (Double.isInfinite(result) || Double.isNaN(result)) {
    String resultStr = Double.toString(result);
    if (resultStr.equals("Infinity")) {
      return Node.newString("Infinity");
    } else if (resultStr.equals("-Infinity")) {
      return new Node(Token.NEG, Node.newString("Infinity"));
    } else {
      return Node.newString("NaN");
    }
  }

  return Node.newNumber(result);
}
