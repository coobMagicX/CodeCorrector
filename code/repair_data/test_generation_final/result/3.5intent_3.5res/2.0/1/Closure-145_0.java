private boolean isOneExactlyFunctionOrDo(Node n) {
    // Check if the node is a FUNCTION or DO
    if (n.getType() == Token.FUNCTION || n.getType() == Token.DO) {
        // If the node is a DO, check if it is within an if statement
        if (n.getType() == Token.DO && n.getParent() != null && n.getParent().getType() == Token.IF) {
            // If the DO node is within an if statement, return false
            return false;
        }
        // If the node is a FUNCTION or a DO node that is not within an if statement,
        // check if it has any child nodes
        if (n.hasChildren()) {
            // Iterate through the child nodes and check if any of them are BLOCK nodes
            for (Node child : n.children()) {
                if (child.getType() == Token.BLOCK) {
                    // If a BLOCK node is found, return false
                    return false;
                }
            }
        }
        // If the node is a FUNCTION or a DO node that is not within an if statement and
        // does not have any child BLOCK nodes, return true
        return true;
    }
    // If the node is not a FUNCTION or a DO node, return false
    return false;
}