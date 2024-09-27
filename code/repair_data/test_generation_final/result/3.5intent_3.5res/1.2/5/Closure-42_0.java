Node processForInLoop(ForInLoop loopNode) {
  // Check if the loop is a 'for each' loop
  if (loopNode.isForEach()) {
    throw new UnsupportedOperationException("The 'for each' loop syntax is not supported.");
  }
  
  // Check if the loop has an iterator
  if (loopNode.getIterator() == null) {
    throw new NullPointerException("The loop must have an iterator.");
  }
  
  // Check if the loop has an iterated object
  if (loopNode.getIteratedObject() == null) {
    throw new NullPointerException("The loop must have an iterated object.");
  }
  
  // Check if the loop has a body
  if (loopNode.getBody() == null) {
    throw new NullPointerException("The loop must have a body.");
  }
  
  // Return the bare minimum to put the AST in a valid state.
  return newNode(
      Token.FOR,
      transform(loopNode.getIterator()),
      transform(loopNode.getIteratedObject()),
      transformBlock(loopNode.getBody()));
}