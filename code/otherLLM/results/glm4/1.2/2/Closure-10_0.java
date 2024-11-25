static boolean mayBeStringHelper(Node n) {
    if (n.getType() == Token.STRING) {
        // If the node is a string, we should consider it as a potential string.
        return true;
    } else if (!isNumericResult(n) && !isBooleanResult(n) && !isUndefined(n)) {
        // Check for other cases where the node might be considered a string.
        switch (n.getType()) {
            case Token.NAME:
                // For names, we consider them as strings unless they are known numeric constants or functions that return numbers.
                String name = n.getString();
                if (!name.equals("true") && !name.equals("false") && !name.equals("null") &&
                    !name.equals("Infinity") && !name.equals("NaN")) {
                    return true;
                }
                break;
            case Token.LITERAL:
            case Token.COMMA:
            case Token.HOOK:
            // These are string-like nodes that may represent strings.
                return true;
            default:
                // If the node is not a recognized numeric, boolean, or undefined, and none of its children match the criteria,
                // we need to check recursively if it has any children.
                if (n.getChildCount() > 0) {
                    for (Node child : n.getChildren()) {
                        if (mayBeStringHelper(child)) {
                            return true;
                        }
                    }
                }
        }
    }
    return false; // If none of the above conditions are met, it's not a string.
}