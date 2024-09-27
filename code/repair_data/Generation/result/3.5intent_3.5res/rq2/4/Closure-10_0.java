static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n);
    }
}

static boolean allResultsMatch(Node n, Predicate<Node> predicate) {
    if (n instanceof BinaryOperation) {
        BinaryOperation binaryOperation = (BinaryOperation) n;
        return allResultsMatch(binaryOperation.getLeftOperand(), predicate) &&
                allResultsMatch(binaryOperation.getRightOperand(), predicate);
    } else if (n instanceof TernaryOperation) {
        TernaryOperation ternaryOperation = (TernaryOperation) n;
        return allResultsMatch(ternaryOperation.getCondition(), predicate) &&
                allResultsMatch(ternaryOperation.getTrueValue(), predicate) &&
                allResultsMatch(ternaryOperation.getFalseValue(), predicate);
    } else {
        return predicate.test(n);
    }
}

static boolean mayBeStringHelper(Node n) {
    if (n instanceof Literal) {
        Literal literal = (Literal) n;
        return literal.getValue() instanceof String;
    } else if (n instanceof BinaryOperation) {
        BinaryOperation binaryOperation = (BinaryOperation) n;
        return mayBeStringHelper(binaryOperation.getLeftOperand()) ||
                mayBeStringHelper(binaryOperation.getRightOperand());
    } else if (n instanceof TernaryOperation) {
        TernaryOperation ternaryOperation = (TernaryOperation) n;
        return mayBeStringHelper(ternaryOperation.getCondition()) ||
                mayBeStringHelper(ternaryOperation.getTrueValue()) ||
                mayBeStringHelper(ternaryOperation.getFalseValue());
    } else {
        return false;
    }
}