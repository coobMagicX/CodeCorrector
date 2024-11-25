Node processForInLoop(ForInLoop loopNode) {

    // Check if the for-in loop is in the "for each" syntax.
    if (loopNode.getIterator().isIdentifier()) {
        String iteratedObject = loopNode.getIterator().getValue();
        
        // If it's a "for each" loop, transform it to a regular for loop.
        Node newIterator = newNode(
            Token.VARIABLE,
            null,
            iteratedObject,
            null
        );
        
        ForInLoop newLoopNode = new ForInLoop(newIterator, loopNode.getIteratedObject(), loopNode.getBody());
        return processForInLoop(newLoopNode);
    } else {
        // Return the bare minimum to put the AST in a valid state.
        return newNode(
            Token.FOR,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    }
}