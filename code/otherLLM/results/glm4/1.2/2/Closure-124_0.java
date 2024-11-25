private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    
    Preconditions.checkArgument(node.isGetProp());

    Node childNode = node.getFirstChild();
    while (childNode != null) {
        if (childNode.isName()
            && isNameAssignedTo(childNode.getString(), replacement)) {
            return false; // Early exit if the replacement is not safe.
        }
        
        childNode = childNode.getNext(); // Corrected missing semicolon and moved it outside the conditional block.
    }

    return true;
}