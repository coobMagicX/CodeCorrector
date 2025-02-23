Node processForInLoop(ForInLoop loopNode) {
    // This approach transforms the loop using the FOR token, assuming specific methods need to be defined correctly.
    Node iterator = transform(loopNode.getIterator());
    Node iteratedObject = transform(loopNode.getIteratedObject());
    Node body = transformBlock(loopNode.getBody());

    // Creates a node for the 'for-in' loop where the FOR token is used by convention
    // The structure needs to ideally match how your AST expects a