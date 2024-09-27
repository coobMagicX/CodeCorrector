Node processForInLoop(ForInLoop loopNode) {
    // Ensure the body of the loop is always a block node
    Node body = transformBlock(loopNode.getBody());

    // Return the properly structured Node for a FOR-IN loop
    // Adjust the structure to meet FOR-IN loop requirements based on the test case name
    return newNode(
        Token.FOR_IN,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        body);
}