Node processForInLoop(ForInLoop loopNode) {
    Node iterator = transform(loopNode.getIterator());

    // Ensure a VAR node is used if the iterator does not already include one.
    // This step ensures that variables are properly declared and scoped within the loop.
    if (!iterator.isVar()) {
        iterator = newNode(Token.VAR, transform(loopNode.getIterator()));
    }

    return newNode(
        Token.FOR,
        iterator,
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}
