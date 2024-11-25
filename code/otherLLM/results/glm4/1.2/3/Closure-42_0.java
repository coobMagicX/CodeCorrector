Node processForInLoop(ForInLoop loopNode) {
    // Check if the iterator and iterated object are not null before transforming them
    Node iterator = (loopNode.getIterator() != null) ? transform(loopNode.getIterator()) : newNode(Token.NULL);
    Node iteratedObject = (loopNode.getIteratedObject() != null) ? transform(loopNode.getIteratedObject()) : newNode(Token.NULL);

    // Create a valid block node for the body of the loop
    Block blockBody = new Block();
    List<Node> blockStatements = new ArrayList<>();

    // Process each statement in the loop's body to ensure they are correctly transformed into AST nodes
    for (Node stmt : loopNode.getBody().getStatements()) {
        Node transformedStmt = transform(stmt);
        if (transformedStmt != null) { // Only add non-null statements to maintain a valid AST structure
            blockStatements.add(transformedStmt);
        }
    }

    // Set the processed statements into the block node
    blockBody.setStatements(blockStatements);

    // Return the new for-in loop node with the correct children and transformed body
    return newNode(
        Token.FOR,
        iterator,  // Use the non-null iterator or a NULL node if it was null
        iteratedObject,  // Use the non-null iterated object or a NULL node if it was null
        transformBlock(blockBody)); // Transform the block body and use the resulting node
}