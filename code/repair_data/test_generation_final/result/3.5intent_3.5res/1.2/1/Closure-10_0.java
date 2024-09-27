static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n);
    }
}

static boolean mayBeStringHelper(Node n) {
    switch (n.getType()) {
        case Token.ADD:
            return mayBeString(n.getFirstChild(), true) && mayBeString(n.getLastChild(), true);
        default:
            return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    }
}