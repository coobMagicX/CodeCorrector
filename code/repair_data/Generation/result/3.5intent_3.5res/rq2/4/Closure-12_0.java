private boolean hasExceptionHandler(Node cfgNode) {
    // Parse through the node and check for 'catch' statements
    Node child = cfgNode.getFirstChild();
    while (child != null) {
        if (child.isTry()) {
            Node catchNode = child.getNext();
            if (catchNode != null && catchNode.isCatch()) {
                return true;
            }
        }
        child = child.getNext();
    }
    
    // If no 'catch' statements are found, check if the node has children
    if (cfgNode.hasChildren()) {
        // Recursively check each child node for an exception handler
        for (Node childNode : cfgNode.children()) {
            if (hasExceptionHandler(childNode)) {
                return true;
            }
        }
    }
    
    // If no 'catch' statements are found in the node or its children, return false
    return false;
}