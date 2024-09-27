Node processForInLoop(ForInLoop loopNode) {
    // check for unsupported language extension: for each
    if(loopNode.getIterator() instanceof ForEachLoop) {
        parseError("unsupported language extension: for each");
        return null;
    }
    
    // Return the bare minimum to put the AST in a valid state.
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}