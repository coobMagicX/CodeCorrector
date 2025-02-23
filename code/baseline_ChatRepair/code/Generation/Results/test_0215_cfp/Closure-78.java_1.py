private Node performArithmeticOp(int opType, Node left, Node right) {
  // ... existing comment explanation ...

  Double lValObj = NodeUtil.getNumberValue(left);
  if (lValObj == null) {
    return null; // guard against non-numeric values
  }
  Double rValObj = NodeUtil.getNumberValue(right);
  if (rValObj == null) {
    return null; // guard against non-numeric values
  }

  // ensure these are declared before switch
  double lval = lValObj;
  double rval = rValObj;
  double result;  // moved declaration outside of the switch

  // Adjust the switch to handle zero division separately for DIV and MOD
  if (opType == Token.DIV || opType == Token.MOD) {
    if (rval == 0) {
      error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
      return null; // early return for zero division
    }
  }

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
      // This is handled above with an early return
      result = lval % rval;
      break;
    case Token.DIV:
      // This is handled above with an early return
      result = lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  // Continue with post-processing the result
  if (Double.isNaN(result)) {
    return Node.newString(Token.NAME, "NaN");
  } else if (result == Double.POSITIVE_INFINITY) {
    return Node.newString(Token.NAME, "Infinity");
  } else if (result == Double.NEGATIVE_INFINITY) {
    return new Node(Token.NEG, Node.newString(Token.NAME, "Infinity"));
  } else {
    Node newNumber = Node.newNumber(result);
    return newNumber;
  }
}
