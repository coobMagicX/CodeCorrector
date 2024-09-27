static boolean evaluatesToLocalValue(Node value, Predicate<Node> locals) {
    switch (value.getType()) {
        case Token.ASSIGN:
            return NodeUtil.isImmutableValue(value.getLastChild())
                    || (locals.apply(value) && evaluatesToLocalValue(value.getLastChild(), locals));
        case Token.COMMA:
            return evaluatesToLocalValue(value.getLastChild(), locals);
        case Token.AND:
        case Token.OR:
            return evaluatesToLocalValue(value.getFirstChild(), locals)
                    && evaluatesToLocalValue(value.getLastChild(), locals);
        case Token.HOOK:
            return evaluatesToLocalValue(value.getFirstChild().getNext(), locals)
                    && evaluatesToLocalValue(value.getLastChild(), locals);
        case Token.INC:
        case Token.DEC:
            if (value.getBooleanProp(Node.INCRDECR_PROP)) {
                return evaluatesToLocalValue(value.getFirstChild(), locals);
            } else {
                return true;
            }
        case Token.THIS:
            return locals.apply(value);
        case Token.NAME:
            return isImmutableValue(value) || locals.apply(value);
        case Token.GETELEM:
        case Token.GETPROP:
            return locals.apply(value);
        case Token.CALL:
            return callHasLocalResult(value)
                    || isToStringMethodCall(value)
                    || locals.apply(value);
        case Token.NEW:
            if (isNewObjectLocal(value)) {
                return true;
            } else {
                return false;
            }
        case Token.FUNCTION:
        case Token.REGEXP:
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            return true;
        case Token.IN:
            return true;
        default:
            throw new IllegalStateException(
                    "Unexpected expression node" + value +
                            "\n parent:" + value.getParent());
    }
}

static boolean isNewObjectLocal(Node value) {
    // Check if the new object aliases itself
    return !NodeUtil.isAliased(value);
}