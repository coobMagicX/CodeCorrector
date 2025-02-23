private Node performArithmeticOp(int opType, Node left, Node right) {
  if (opType == Token.ADD && (NodeUtil.mayBeString(left, false) || NodeUtil.mayBeString(right, false))) {
    return null; // Skip arithmetic for string types in ADD since it could be concatenation.
  }

  Double lValObj = NodeUtil.getNumberValue(left);
  Double rValObj = NodeUtil.getNumberValue(right);
  if (lValObj == null || rValObj == null) {
    return null; // If either value is not a number, return null.
  }

  double lval = lValObj;
  double rval = rValObj;
  double result = Double.NaN; // Start with 'Not-a-Number' to handle undefined operations.

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
        result = Double.NaN; // Define modulus zero as NaN for JavaScript compatibility.
      } else {
        result = lval % rval;
      }
      break;
    case Token.DIV:
      if (rval == 0) {
        if (lval == 0) {
          result = Double.NaN;
        } else {
          result = lval > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
      } else {
        result = lval / rval;
      }
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  // Check and create appropriate Node types based on result values.
  if (Double.isNaN(result)) {
    return Node.newString("NaN");
  } else if (result == Double.POSITIVE_INFINITY) {
    return Node.newString("Infinity");
  } else if (result == Double.NEGATIVE_INFINITY) {
    return Node.newString("-Infinity");
  } else {
    return Node.newNumber(result);
  }
}
