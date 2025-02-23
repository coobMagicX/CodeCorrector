Node processForInLoop(ForInLoop loopNode) {
    Node iterator = transform(loopNode.getIterator());

    // Create a VAR node if the iterator is not already one for correct scoping
    if (!iterator.hasChildren()) {
        iterator = newNode(Token.VAR, iterator);
    }

    Node iteratedObject = transform(loopNode.getIteratedObject());
    Node body = transformBlock(loopNode.getBody());

