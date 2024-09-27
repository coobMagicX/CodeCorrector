private void findCalledFunctions(Node node, Set<String> changed) {
    Preconditions.checkArgument(changed != null);
    // Traverse through the node to identify function calls
    if (node.getType() == Token.CALL) {
        Node child = node.getFirstChild();
        // Check if the child node is a name, a function expression (IIFE), or a property access
        if (child.getType() == Token.NAME) {
            changed.add(child.getString());
        } else if (child.getType() == Token.FUNCTION) {
            // Handle directly invoked function expressions (IIFE)
            if (child.hasChildren()) {
                // Traverse the function body to find internal calls
                findCalledFunctions(child.getLastChild(), changed);
            }
        } else if (child.getType() == Token.GETPROP) {
            // Handle property access on objects that might be function calls
            if (child.getLastChild().getType() == Token.STRING) {
                changed.add(child.getLastChild().getString());
            }
        }
    }

    // Recursive call to traverse through all child nodes
    for (Node c = node.getFirstChild(); c != null; c = c.getNext()) {
        findCalledFunctions(c, changed);
    }
}