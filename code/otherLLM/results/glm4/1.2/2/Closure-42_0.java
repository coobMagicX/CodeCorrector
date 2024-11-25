Node processForInLoop(ForInLoop loopNode) {
    if (loopNode == null) {
        throw new IllegalArgumentException("ForInLoop node cannot be null");
    }

    // Check if the 'for each' syntax is supported.
    String iteratorSymbol = transform(loopNode.getIterator());
    if (iteratorSymbol == null || !iteratorSymbol.equals("forEach")) {
        throw new IllegalStateException("Unsupported or invalid 'for each' loop syntax: " + iteratorSymbol);
    }

    // Transform the iterated object to ensure it's in a valid form for processing.
    Node iteratedObject = transform(loopNode.getIteratedObject());
    if (iteratedObject == null) {
        throw new IllegalStateException("The iterated object in 'for each' loop is invalid or unsupported.");
    }

    // Process the body of the loop
    Node loopBody = transformBlock(loopNode.getBody());

    // Return the constructed node for the 'for each' loop.
    return newNode(
        Token.FOR,
        iteratorSymbol,
        iteratedObject,
        loopBody);
}