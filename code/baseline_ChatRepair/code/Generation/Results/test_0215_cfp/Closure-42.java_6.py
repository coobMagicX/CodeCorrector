Node processForInLoop(ForInLoop loopNode) {
    if (loopNode.isForOf()) {
        // Process as for...of loop
        return newNode(
            Token.FOR_OF,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode