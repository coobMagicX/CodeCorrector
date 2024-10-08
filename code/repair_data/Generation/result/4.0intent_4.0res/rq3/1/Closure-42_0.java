Node processForInLoop(ForInLoop loopNode) {

    // Return the bare minimum to put the AST in a valid state.
    return newNode(
        Token.FOR_IN,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}