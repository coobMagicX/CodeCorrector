private boolean hasExceptionHandler(Node cfgNode) {
    // Check if the node is an instance of TryCatchBlock
    if (cfgNode instanceof TryCatchBlock) {
        return true;
    }
    
    // Check for children nodes in case of a compound statement like IfStatement or DoLoop
    List<Node> children = cfgNode.getChildren();
    for (Node child : children) {
        // Recursively check each child node to see if it is a TryCatchBlock
        if (hasExceptionHandler(child)) {
            return true;
        }
    }
    
    // No exception handlers found
    return false;
}