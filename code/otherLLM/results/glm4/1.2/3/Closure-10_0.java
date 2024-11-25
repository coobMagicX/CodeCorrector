static boolean mayBeString(Node n, boolean recurse) {
    if (recurse) {
        // Check if the node itself is a string-like construct.
        if (n.getType() == Token.STRING) {
            return true;
        }

        // For recursive check, ensure that expressions involving logical operators
        // and conditional operators are also checked for string-like results.
        TernaryValue value = getImpureBooleanValue(n);
        if (value.isString()) {
            return true;
        }

        // If the node is an expression, check if it's a string-like expression.
        if (n.getType() == Token.EXPR) {
            // Check for logical operators and conditional expressions
            Node[] children = n.getChildren();
            for (Node child : children) {
                if (child.getType().matches(Token.NOT | Token.AND | Token.OR)) {
                    TernaryValue childValue = getImpureBooleanValue(child);
                    if (childValue.isString()) {
                        return true;
                    }
                }
            }
        }

        // Otherwise, recursively check the children nodes.
        return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
    } else {
        return mayBeStringHelper(n);
    }
}