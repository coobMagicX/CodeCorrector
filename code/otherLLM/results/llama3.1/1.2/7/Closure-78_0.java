private Node performArithmeticOp(int opType, Node left, Node right) {
  // Unlike other operations, ADD operands are not always converted
  // to Number.
  if (opType == Token.ADD
      && (NodeUtil.mayBeString(left, false)
          || NodeUtil.mayBeString(right, false))) {
    return null;
  }

  double result;

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
        return null;
      }
      result = lval % rval;
      break;
    case Token.DIV:
      if (rval == 0) {
        error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
        return null;
      } else if (!Double.isInfinite(rval)) {
        result = lval / rval;
      } else {
        // If the divisor is infinite, we should return Infinity or -Infinity.
        if (rval > 0) {
          result = Double.POSITIVE_INFINITY;
        } else {
          result = Double.NEGATIVE_INFINITY;
        }
      }
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  // TODO(johnlenz): consider removing the result length check.
  // length of the left and right value plus 1 byte for the operator.
  if (String.valueOf(result).length() <=
      String.valueOf(lval).length() + String.valueOf(rval).length() + 1 &&

      // Do not try to fold arithmetic for numbers > 2^53. After that
      // point, fixed-point math starts to break down and become inaccurate.
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