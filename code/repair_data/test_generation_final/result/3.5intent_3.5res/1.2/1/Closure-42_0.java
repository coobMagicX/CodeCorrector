Node processForInLoop(ForInLoop loopNode) {
    if (loopNode.isForEach()) {
        parseError("Unsupported language extension: for each loop");
    }

    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody())
    );
}