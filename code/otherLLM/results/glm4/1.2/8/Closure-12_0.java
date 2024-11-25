private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode == null) return false;

    // Assuming Node has a method called 'isTryBlock' that returns true if it is a try block.
    // Also assuming Node has a method called 'getCatchBlocks' that returns a list of catch blocks.

    // Check if the node itself is a try block
    if (cfgNode.isTryBlock()) {
        return true;
    }

    // Check for catch blocks in this node
    List<Node> catchBlocks = cfgNode.getCatchBlocks();
    if (!catchBlocks.isEmpty()) {
        return true;
    }

    // Recursively check child nodes
    List<Node> children = cfgNode.getChildren(); // Assuming Node has a method 'getChildren' that returns its child nodes.
    for (Node child : children) {
        if (hasExceptionHandler(child)) {
            return true; // Found an exception handler in a child node
        }
    }

    return false; // No exception handlers found
}