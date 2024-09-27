private boolean isOneExactlyFunctionOrDo(Node n) {
    // Check if the node is a FUNCTION, DO, or an empty statement which might be needed for older browsers
    if (n.getType() == Token.FUNCTION || n.getType() == Token.DO || n.isEmpty()) {
        // For labels with block children, ensure proper handling
        Node parent = n.getParent();
        if (parent != null && parent.getType() == Token.LABEL) {
            Node grandparent = parent.getParent();
            if (grandparent != null && grandparent.getType() == Token.BLOCK) {
                // If the block under the label has more than one child, it's not exclusively a FUNCTION, DO, or an empty statement
                return grandparent.hasOneChild();
            }
        }
        // Return true if not under a label with a problematic block
        return true;
    }
    // Return false if the node is neither FUNCTION nor DO, nor is it an empty statement
    return false;
}