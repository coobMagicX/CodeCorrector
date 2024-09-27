Node processForInLoop(ForInLoop loopNode) {
    // Check if the loop is a "for each" loop
    if (loopNode.isForEach()) {
        // Process "for each" loop similarly to a standard for-in but with an additional flag or comment
        Node forEachNode = newNode(
            Token.FOR_EACH,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
        forEachNode.putProp(Node.FOR_EACH_PROP, true); // Indicate this node was originally a 'for each' loop
        return forEachNode;
    }

    // Return the transformed node for a standard for-in loop
    return newNode(
        Token.FOR,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
}