Node processForInLoop(ForInLoop loopNode) {
    // Check if the loopNode is a ForEachLoop
    if (loopNode instanceof ForEachLoop) {
        // Handle the ForEachLoop differently
        ForEachLoop forEachLoop = (ForEachLoop) loopNode;
        return newNode(
            Token.FOR_EACH,
            transform(forEachLoop.getVariable()),
            transform(forEachLoop.getIterable()),
            transformBlock(forEachLoop.getBody()));
    } else {
        // Handle the regular ForInLoop
        return newNode(
            Token.FOR,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    }
}