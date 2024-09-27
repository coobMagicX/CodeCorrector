Node processForInLoop(ForInLoop loopNode) {
    if (loopNode.isForEach()) {
        // Assuming 'forEach' is being used in a context like:
        // for each (var item in items) { ... }
        // We transform it to a standard for-in loop for compatibility
        return newNode(
            Token.FOR,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    }

    // Return the node structure for a standard 'for in' loop
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}