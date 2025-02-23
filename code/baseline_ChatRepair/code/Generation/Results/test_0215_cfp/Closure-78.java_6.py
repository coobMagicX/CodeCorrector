...
case Token.MOD:
case Token.DIV:
  if (rval == 0) {
    // Properly propagate the error information when division or modulus by zero occurs
    error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
    return Node.newString("DIVIDE_BY_ZERO_ERROR");
  }
  result = opType == Token.MOD ? lval % rval : lval / rval;
  break;
...
