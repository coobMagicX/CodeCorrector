Node processForInLoop(ForInLoop loopNode) {
    Node iterator = transform(loopNode.getIterator());
    Node iteratedObject = transform(loopNode.getIteratedObject());
    Node body = transformBlock(loopNode.getBody());

    // Validate if the iterator and iterated object are not null
    if (iterator != null && iteratedObject != null) {
        // Return a new FOR node with the transformed elements
        return newNode(
            Token.FOR,
            iterator,
            iteratedObject,
            body
        );
    } else {
        // Return null if either the iterator or iterated object is null
        return null;
    }
}

private Node transformBlock(AstNode node) {
    Node irNode = transform(node);
    if (!irNode.isBlock()) {
        if (irNode.isEmpty()) {
            irNode.setType(Token.BLOCK);
            irNode.setWasEmptyNode(true);
        } else {
            Node newBlock = newNode(Token.BLOCK, irNode);
            newBlock.setLineno(irNode.getLineno());
            newBlock.setCharno(irNode.getCharno());
            maybeSetLengthFrom(newBlock, node);
            irNode = newBlock;
        }
    }
    return irNode;
}