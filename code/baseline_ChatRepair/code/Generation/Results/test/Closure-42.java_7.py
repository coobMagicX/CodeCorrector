Node processForInLoop(ForInLoop loopNode) {
    Token loopType = loopNode.isForOf() ? Token.FOR_OF : Token.FOR_IN;
    
    return newNode(
        loopType,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}
