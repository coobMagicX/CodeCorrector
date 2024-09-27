static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n);
    }
}

static final Predicate<Node> MAY_BE_STRING_PREDICATE = new Predicate<Node>() {
    @Override
    public boolean apply(Node n) {
        switch (n.getType()) {
            case Token.ADD:
                return mayBeString(n.getFirstChild(), false) || mayBeString(n.getLastChild(), false);
            case Token.ASSIGN:
            case Token.COMMA:
                return allResultsMatch(n.getLastChild(), this);
            case Token.AND:
            case Token.OR:
                return allResultsMatch(n.getFirstChild(), this) && allResultsMatch(n.getLastChild(), this);
            case Token.HOOK:
                return allResultsMatch(n.getFirstChild().getNext(), this) && allResultsMatch(n.getLastChild(), this);
            default:
                return n.getType() == Token.STRING;
        }
    }
};
