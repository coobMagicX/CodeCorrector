private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode.getExceptionHandlers() != null && !cfgNode.getExceptionHandlers().isEmpty()) {
        return true;
    } else {
        for (Statement statement : cfgNode.getStatements()) {
            if (statement.hasTryCatchBlock()) {
                return true;
            }
        }
    }
    return false;
}

private boolean someFunction(Node node) {
    // ...
}

private boolean noInline() {
    return true;
}