Node processForInLoop(ForInLoop loopNode) {
    Node iteratorTransform = transform(loopNode.getIterator());
    Node objectTransform = transform(loopNode.getIteratedObject());
    Node bodyTransform = transformBlock(loopNode.getBody());

    if (loopNode.isForEach()) {
       