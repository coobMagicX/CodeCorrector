Node processForInLoop(ForInLoop loopNode) {
    if (loopNode.isForEach()) {
        // Handle "for each" loop
        return newNode(
            Token.FOR_EACH,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    } else {
        // Handle regular "for in" loop
        return newNode(
            Token.FOR,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    }
}