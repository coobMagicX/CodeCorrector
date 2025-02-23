private Node performArithmeticOp(int opType, Node left, Node right) {
  // Unlike other operations, ADD operands are not always converted
  // to Number.
  if (opType == Token.ADD
      && (NodeUtil.mayBeString(left, false)
          || NodeUtil.mayBeString(right, false))) {
    return null;
  }

  Double lValObj = NodeUtil.getNumberValue(left);
  Double rValObj = NodeUtil.getNumberValue(right);
  if (lValObj == null || rValObj == null) {
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
        result = Double.NaN; // Assign NaN to result when divisor is 0 for modulus
        break;
      }
      result = lval % rval;
      break;
    case Token.DIV:
      if (rval == 0) {
        result = lval >= 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY; // Handle positive and negative infinity
        break;
      }
      result = lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  if (Double.isNaN(result)) {
    return Node.newString(Token.NAME, "NaN");
  } else if (result == Double.POSITIVE_INFINITY) {
    return Node.newString(Token.NAME, "Infinity");
  } else if (result == Double.NEGATIVE_INFINITY) {
    return new Node(Token.NEG, Node.newString(Token.NAME, "Infinity"));
  } else if (Math.abs(result) <= MAX_FOLD_NUMBER) {
    return Node.newNumber(result);
  }

  return null;
}
