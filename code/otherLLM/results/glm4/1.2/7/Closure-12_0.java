private boolean hasExceptionHandler(Node cfgNode) {
    // Check if the node is an instance of TryStatement which indicates a try block.
    if (cfgNode instanceof TryStatement) {
        return true; // If it is, there's an exception handler, so return true.
    }

    // If the node is not a try statement, check for catch blocks within this node.
    List<CatchBlock> catchBlocks = cfgNode.getCatchBlocks();
    if (catchBlocks != null && !catchBlocks.isEmpty()) {
        return true; // There's at least one catch block, so there's an exception handler.
    }

    // Recursively check the children nodes to see if any of them have an exception handler
    for (Node child : cfgNode.getChildren()) {
        if (hasExceptionHandler(child)) { // Recursive call
            return true; // If any child has an exception handler, propagate this info up.
        }
    }

    return false; // No exception handlers found in this node or its children.
}