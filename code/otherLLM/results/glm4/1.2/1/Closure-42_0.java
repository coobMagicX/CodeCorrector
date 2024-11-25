// Assuming newNode, transform, and transformBlock are defined elsewhere in your codebase
// and are capable of creating a new AST node with the given tokens.

Node processForInLoop(ForInLoop loopNode) {
    // Check if the language extension is supported by throwing an error message
    // if 'for each' loop syntax is detected. Otherwise, proceed to create the AST node.
    if (!supportsForEachLoop()) {
        throw new UnsupportedOperationException("The 'for each' loop syntax is not supported.");
    }

    // Return the bare minimum to put the AST in a valid state.
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}

// Dummy implementations of the methods that are assumed to be defined elsewhere:

boolean supportsForEachLoop() {
    // This method should check if the 'for each' loop syntax is supported by the system.
    // It's a placeholder for whatever logic you have in your codebase to determine this.
    return true;
}

Node newNode(Token token, Node iterator, Node iteratedObject, Node block) {
    // This method creates a new AST node given its token and children nodes.
    // The implementation would typically involve creating a class that represents an AST node
    // and setting the relevant fields on it. Since we're not allowed to create any new classes,
    // this is just a placeholder for whatever actual logic you have.
    return new Node(token, iterator, iteratedObject, block);
}

Node transform(Expression expression) {
    // This method transforms an expression into an AST node.
    // Placeholder implementation.
    return newNode(Token.EXPRESSION, null, null, null);
}

Node transformBlock(BlockStatement block) {
    // This method transforms a block of statements into an AST node.
    // Placeholder implementation.
    return newNode(Token.BLOCK, null, null, null);
}