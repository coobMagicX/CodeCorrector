private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode == null) {
        return false;
    }
    for (Node child : cfgNode.getChildren()) {
        if (child instanceof TryStatement) {
            return true;
        }
        if (hasExceptionHandler(child)) {
            return true;
        }
    }
    return false;
}