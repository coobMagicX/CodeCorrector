private boolean hasExceptionHandler(Node cfgNode) {
    // Check if the CFG node has an exception handler
    if (cfgNode.getExceptionHandler() != null) {
        return true;
    } else {
        return false;
    }
}