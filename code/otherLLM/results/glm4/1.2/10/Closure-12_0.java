private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode instanceof TryStatement) {
        return true; // Found a try block, which means there is an exception handler.
    } else if (cfgNode instanceof CatchClause) {
        return true; // Found a catch clause, which means there is an exception handler.
    } else {
        for (Node child : cfgNode.getChildren()) {
            if (hasExceptionHandler(child)) {
                return true; // Recursively check children nodes
            }
        }
    }
    return false; // No try or catch blocks found in this node or its children
}