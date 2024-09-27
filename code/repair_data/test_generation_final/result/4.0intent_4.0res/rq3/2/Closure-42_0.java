Node processForInLoop(ForInLoop loopNode) {
    // Ensure that the loop is transformed into a valid Node for a For-In loop
    return newNode(
        Token.FOR_IN,                       // Correct token type for For-In loop
        transform(loopNode.getIterator()),   // Transform the iterator
        transform(loopNode.getIteratedObject()), // Transform the object being iterated over
        transformBlock(loopNode.getBody())); // Transform the body of the loop
}