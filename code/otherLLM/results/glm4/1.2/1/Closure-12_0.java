private boolean hasExceptionHandler(Node cfgNode) {
    // Check if the current node is an exception handler.
    if (cfgNode.isExceptionHandler()) {
        return true;
    }

    // If not, check if there are any children nodes which might represent exception handlers.
    for (Node child : cfgNode.getChildren()) {
        if (hasExceptionHandler(child)) { // Recursively search in the child nodes
            return true;
        }
    }

    // No exception handler found in this path of the CFG.
    return false;
}