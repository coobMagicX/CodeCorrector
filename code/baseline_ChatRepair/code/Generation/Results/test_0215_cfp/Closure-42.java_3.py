Node processForInLoop(ForInLoop loopNode) {
    Node iterator = transform(loopNode.getIterator());

    // Ensure iterator is correctly setup for use in For...in loop
    if (!iterator.isVar()) {
        // Create a VAR node to properly declare the iterator in the loop if it's not already a VAR
        iterator = newNode(Token.VAR).addChildToBack(transform(loopNode.getIterator()));
