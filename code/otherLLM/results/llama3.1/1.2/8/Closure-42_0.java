Node processForInLoop(ForInLoop loopNode) {

    // Implement proper handling for `for each` loops
    if (loopNode.getIterator().isForEach()) {
        // Iterate over the object properties and create a new loop structure
        Iterable<PropertyAccess> properties = loopNode.getIteratedObject().getProperties();
        Node iteratedObject = newNode(Token.IDENTIFIER, "");
        for (PropertyAccess property : properties) {
            iteratedObject.addChild(transform(property));
        }
        
        // Return the transformed AST
        return newNode(
            Token.FOR_EACH,
            transform(loopNode.getIterator()),
            iteratedObject,
            transformBlock(loopNode.getBody()));
    } else {
        // If not a `for each` loop, process it as usual
        return newNode(
            Token.FOR,
            transform(loopNode.getIterator()),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    }
}