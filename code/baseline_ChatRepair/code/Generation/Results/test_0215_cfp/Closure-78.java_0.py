private Node performArithmeticOp(int opType, Node left, Node right) {
  // ... existing code ...

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
        return null; // explicitly return
      }
      result = lval % rval;
      break;
    case Token.DIV:
      if (rval == 0) {
        error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
        return null; // explicitly return
      }
      result = lval / rval;
      break;
    default:
      throw new Error("Unexpected arithmetic operator");
  }

  // ... existing code ...

  return null; // catch-all return at the end if needed, for methods requiring a return value
}
