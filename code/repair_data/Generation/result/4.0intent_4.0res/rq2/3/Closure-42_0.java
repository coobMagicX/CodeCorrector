Node processForInLoop(ForInLoop loopNode) {
    if (loopNode.isForEach()) {  // Check if the loop is a "for each" loop
        throw new UnsupportedOperationException("Unsupported language extension: for each");
    }

    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}