private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode instanceof TryStatement) {
        return true; // Recognize try blocks as exception handlers
    } else if (cfgNode instanceof CatchClause) {
        return true; // Recognize catch clauses as exception handlers
    } else if (cfgNode instanceof FinallyBlock) {
        return true; // Recognize finally blocks as exception handlers
    }
    for (Node child : cfgNode.getChildren()) {
        if (hasExceptionHandler(child)) {
            return true;
        }
    }
    return false;
}