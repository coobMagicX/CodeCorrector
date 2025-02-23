Node processForInLoop(ForInLoop loopNode) {
    Node iteratorTransform = transform(loopNode.getIterator());
    Node objectTransform = transform(loopNode.getIteratedObject());
    Node bodyTransform = transformBlock(loopNode.getBody());

    // Assuming a generic FOR token for all loop variations due to possible unsupported FOR_OF
    // We're going to use three children to create the proper FOR structure: iterator, object, and body.
    return newNode(
        Token.FOR, // Using FOR token which may need to be adjusted if your environment supports different tokens
        iteratorTransform,
        objectTransform,
        bodyTransform);
}
