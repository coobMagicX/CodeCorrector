Node processForInLoop(ForInLoop loopNode) {

    // Return the bare minimum to put the AST in a valid state.
    // Check if language extension is supported (e.g., "for each")
    if (loopNode.getExtension() != null && loopNode.getExtension().equals("each")) {
        return processForEach(loopNode);
    } else {
        return newNode(
            Token.FOR,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    }
}

// New method to handle "for each" language extension
private Node processForEach(ForInLoop loopNode) {
    // Specific transformation logic for "for each" extension
    return newNode(
        Token.FOR_EACH,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}