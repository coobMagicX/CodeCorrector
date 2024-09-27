private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode == null) {
        return false;
    }
    for (Node child : cfgNode.getChildren()) {
        if (child.getType() == Node.CATCH) {
            return true;
        }
        // Recursively search in child nodes.
        if (hasExceptionHandler(child)) {
            return true;
        }
    }
    return false;
}