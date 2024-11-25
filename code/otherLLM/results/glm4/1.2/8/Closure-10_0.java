static boolean mayBeStringHelper(Node n) {
    switch (n.getType()) {
        case Token.STRING:
            // Directly returning true as a STRING token is definitely a string literal.
            return true;

        case Token.NAME:
            // Check for known constants which could be strings like "NaN", "Infinity" etc.
            String name = n.getString();
            if (name.equals("undefined") || name.equals("NaN") || name.equals("Infinity")) {
                return false; // These are not string literals.
            }
            return true;

        case Token.NUMBER:
        case Token.BOOLEAN:
        case Token.VOID:
        case Token.NEG:
        case Token.NOT:
        case Token.ARRAYLIT:
        case Token.OBJECTLIT:
        case Token.LABEL:
        case Token.THROW:
        case Token.BREAK:
        case Token.CONTINUE:
            // All these cases are not string literals.
            return false;

        default:
            // For any other type of node, we cannot definitively say it is a string literal,
            // thus we can't guarantee it's a string literal without further processing or recursion.
            return false;
    }
}