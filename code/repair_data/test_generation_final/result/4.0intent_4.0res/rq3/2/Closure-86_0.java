static boolean evaluatesToLocalValue(Node value, Predicate<Node> locals) {
    switch (value.getType()) {
        case Token.ASSIGN:
            // A result that is aliased by a non-local name, is effectively the
            // same as returning a non-local name, but this doesn't matter if the
            // value is immutable.
            return NodeUtil.isImmutableValue(value.getLastChild())
                || (locals.test(value.getFirstChild()) // Fixed to test the first child of the assignment
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
            return locals.test(value); // Changed apply to test
        case Token.NAME:
            return NodeUtil.isImmutableValue(value) || locals.test(value); // Changed apply to test and fixed method call
        case Token.GETELEM:
        case Token.GETPROP:
            // There is no information about the locality of object properties.
            return locals.test(value); // Changed apply to test
        case Token.CALL:
            return callHasLocalResult(value)
                || isToStringMethodCall(value)
                || locals.test(value); // Changed apply to test
        case Token.NEW:
            // TODO(nicksantos): This needs to be changed so that it
            // returns true iff we're sure the value was never aliased from inside
            // the constructor (similar to callHasLocalResult)
            return true;
        case Token.FUNCTION:
        case Token.REGEXP:
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
            // Literal objects with non-literal children are allowed.
            return true;
        case Token.IN:
            // TODO(johnlenz): should IN operator be included in #isSimpleOperator?
            return true;
        default:
            // Other ops force a local value:
            //  x = '' + g (x is now a local string)
            //  x -= g (x is now a local number)
            if (isAssignmentOp(value)
                || isSimpleOperator(value)
                || NodeUtil.isImmutableValue(value)) {
                return true;
            }

            throw new IllegalStateException(
                "Unexpected expression node: " + value +
                "\n parent: " + value.getParent());
    }
}