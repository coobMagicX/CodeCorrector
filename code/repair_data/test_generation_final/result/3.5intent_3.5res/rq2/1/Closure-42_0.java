Node processForInLoop(ForInLoop loopNode) {
    // Return the bare minimum to put the AST in a valid state.
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody(), loopNode.getLineno(), loopNode.getCharno()));
}

private Node transformBlock(AstNode node, int lineno, int charno) {
    Node irNode = transform(node);
    if (!irNode.isBlock()) {
        if (irNode.isEmpty()) {
            irNode.setType(Token.BLOCK);
            irNode.setWasEmptyNode(true);
        } else {
            Node newBlock = newNode(Token.BLOCK, irNode);
            newBlock.setLineno(lineno);
            newBlock.setCharno(charno);
            maybeSetLengthFrom(newBlock, node);
            irNode = newBlock;
        }
    }
    return irNode;
}