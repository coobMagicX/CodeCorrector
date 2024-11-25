Node processForInLoop(ForInLoop loopNode) {

    // Correctly handle language extensions by checking for 'each' keyword
    if (loopNode.getIterator().getKeyword().equals("each")) {
        // Parse and validate 'for each' loops
        Iterator iterator = new ForEachIterator(loopNode.getIterator());
        
        // Update AST transformation to handle 'for each' loop syntax
        return newNode(
            Token.FOR_EACH,  // Use FOR_EACH token for 'for each' loops
            transform(iterator),
            transform(loopNode.getIteratedObject()),
            transformBlock(loopNode.getBody()));
    } else {
        // If not a 'for each' loop, fall back to original logic
        return processForInLoopOriginal(loopNode);
    }
}

// Define the ForEachIterator class and its methods (transform(), etc.)
class ForEachIterator implements Iterator {
    // Implement necessary methods for ForEachIterator
}

// Define the processForInLoopOriginal() method to handle non-'for each' loops
Node processForInLoopOriginal(ForInLoop loopNode) {
    // Return the original logic for processing 'for in' loops
}