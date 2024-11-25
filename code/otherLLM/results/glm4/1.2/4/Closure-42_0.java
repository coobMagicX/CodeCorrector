Node processForInLoop(ForInLoop loopNode) {
    // If the iterator or the iterated object is null, we should handle it appropriately.
    if (loopNode.getIterator() == null || loopNode.getIteratedObject() == null) {
        throw new IllegalArgumentException("Iterator or Iterated Object cannot be null in a for-each loop.");
    }

    // Create a new node to represent the for-each loop and return it.
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}