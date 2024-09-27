static boolean evaluatesToLocalValue(Node value, Predicate<Node> locals) {
    switch (value.getType()) {
        case Token.ASSIGN:
            return NodeUtil.isImmutableValue(value.getLastChild())
                    || (locals.test(value.getFirstChild())
                        && evaluatesToLocalValue(value.getLastChild(), locals));
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
            return locals.test(value);
        case Token.NAME:
            return NodeUtil.isImmutableValue(value) || locals.test(value);
        case Token.GETELEM:
        case Token.GETPROP:
            return locals.test(value);
        case Token.CALL:
            return callHasLocalResult(value)
                    || isToStringMethodCall(value)
                    || locals.test(value);
        case Token.NEW:
            return true;
        case Token.FUNCTION:
        case Token.REGEXP:
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            return true;
        case Token.IN:
            return true;
        default:
            if (NodeUtil.isAssignmentOp(value)
                    || NodeUtil.isSimpleOperator(value)
                    || NodeUtil.isImmutableValue(value)) {
                return true;
            }
            throw new IllegalStateException(
                "Unexpected expression node" + value +
                "\n parent:" + value.getParent());
    }
}