Node processForInLoop(ForInLoop loopNode) {
    // Return the bare minimum to put the AST in a valid state, ensuring 'in' is part of the structure.
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        newNode(Token.IN, transform(loopNode.getIteratedObject())),
        transformBlock(loopNode.getBody()));
}