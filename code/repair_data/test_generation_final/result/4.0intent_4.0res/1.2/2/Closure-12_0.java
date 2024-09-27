private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode == null) {
        return false;
    }

    // Recursively check if any child node contains a try-catch block
    for (Node child : cfgNode.getChildren()) {
        if (child instanceof TryStatement) {
            return true;
        } else if (hasExceptionHandler(child)) {
            return true;
        }
    }
    return false;
}