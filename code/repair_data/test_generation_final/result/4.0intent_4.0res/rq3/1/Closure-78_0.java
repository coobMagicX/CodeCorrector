private Node performArithmeticOp(int opType, Node left, Node right) {
    // Unlike other operations, ADD operands are not always converted
    // to Number.
    if (opType == Token.ADD
        && (NodeUtil.mayBeString(left, false)
            || NodeUtil.mayBeString(right, false))) {
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
                error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
                return null;
            }
            result = lval % rval;
            break;
        case Token.DIV:
            if (rval == 0) {
                error(DiagnosticType.error("JSC_DIVIDE_BY_0_ERROR", "Divide by 0"), right);
                return null;
            }
            result = lval / rval;
            break;
        default:
            throw new Error("Unexpected arithmetic operator");
    }

    // Check for special cases
    if (Double.isNaN(result)) {
        return Node.newString(Token.NAME, "NaN");
    } else if (result == Double.POSITIVE_INFINITY) {
        return Node.newString(Token.NAME, "Infinity");
    } else if (result == Double.NEGATIVE_INFINITY) {
        return new Node(Token.NEG, Node.newString(Token.NAME, "Infinity"));
    }

    // Return the result as a Node
    return Node.newNumber(result);
}