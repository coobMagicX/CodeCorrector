Node processForInLoop(ForInLoop loopNode) {
    Node iteratorTransform = transform(loopNode.getIterator());
    Node objectTransform = transform(loopNode.getIteratedObject());
    Node bodyTransform = transformBlock(loopNode.getBody());

    if (loopNode.isForEach()) {
        // If it's a forEach loop, which is semantically a FOR-OF loop in JavaScript.
        return newNode(
          Token.FOR_OF,
          iteratorTransform,
          objectTransform,
          bodyTransform);
    } else {
        // The default is a FOR-IN loop
        return newNode(
          Token.FOR_IN,
          iteratorTransform,
          objectTransform,
          bodyTransform);
    }
}
