Node processForInLoop(ForInLoop loopNode) {
    // Ensure that iterator and iterated object transformations are correctly handled.
    Node iteratorNode = transform(loopNode.getIterator());
    if (iteratorNode == null || !iteratorNode.isExpression()) {
        parseError("Invalid iterator in for-each loop");
        return newNode(Token.ERROR, Token.NONE);
    }
    
    Node iteratedObjectNode = transform(loopNode.getIteratedObject());
    if (iteratedObjectNode == null || !iteratedObjectNode.isExpression()) {
        parseError("Invalid iterated object in for-each loop");
        return newNode(Token.ERROR, Token.NONE);
    }

    // Check if the body is a valid block.
    Node bodyNode = transformBlock(loopNode.getBody());
    if (bodyNode == null || !bodyNode.isBlock()) {
        parseError("Invalid body in for-each loop");
        return newNode(Token.ERROR, Token.NONE);
    }
    
    // Return the properly constructed for-each loop node.
    return newNode(
        Token.FOR,
        iteratorNode,
        iteratedObjectNode,
        bodyNode
    );
}