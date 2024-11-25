Node processForInLoop(ForInLoop loopNode) {
    // Ensure that the iterator and iterated object are transformed into AST nodes properly.
    Node iteratorNode = transform(loopNode.getIterator());
    Node iteratedObjectNode = transform(loopNode.getIteratedObject());

    // Transform the body of the for-in loop, which should also be a valid node.
    Node blockBodyNode = transformBlock(loopNode.getBody());

    // Check if the iterator and iterated object are not null before returning a new AST node.
    if (iteratorNode != null && iteratedObjectNode != null) {
        return newNode(
            Token.FOR,
            iteratorNode,
            iteratedObjectNode,
            blockBodyNode
        );
    } else {
        // Handle the error case where either iterator or iterated object is missing.
        ErrorReporter reporter = new ErrorReporter();
        reporter.reportError(loopNode.getSourcePosition(), "Iterator or Iterated Object cannot be null in ForInLoop");
        return newNode(Token.ERROR, reporter);
    }
}