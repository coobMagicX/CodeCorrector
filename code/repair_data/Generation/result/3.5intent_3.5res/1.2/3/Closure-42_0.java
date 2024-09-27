Node processForInLoop(ForInLoop loopNode) {
    // Check for unsupported language extension: for each
    if(loopNode.getType().equals("ForEachLoop")) {
        parseError("Unsupported language extension: for each");
    }

    // Return the bare minimum to put the AST in a valid state.
    return newNode(
            Token.FOR,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody())
    );
}