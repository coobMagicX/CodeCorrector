Node processForInLoop(ForInLoop loopNode) {
    // Ensure that FOR-IN structure is created properly
    return newNode(
        Token.FOR_IN, // Use FOR_IN instead of FOR, specifying a for-in loop
        transform(loopNode.getIterator()), // Iterator: the variable part
        transform(loopNode.getIteratedObject()), // Object being iterated over
        transformBlock(loopNode.getBody())); // Body of the loop
}
