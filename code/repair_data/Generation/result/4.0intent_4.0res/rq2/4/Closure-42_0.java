Node processForInLoop(ForInLoop loopNode) {
    // Check if the loopNode is a "for each" type and handle it
    if (loopNode.isForEach()) {
        return newNode(
            Token.FOR_EACH,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    }

    // Return the transformed node structure for a regular For-In loop
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}