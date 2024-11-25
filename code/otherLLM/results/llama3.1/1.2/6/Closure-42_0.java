Node processForInLoop(ForInLoop loopNode) {

  // Check if it's a FOR EACH loop and handle accordingly
  if (loopNode.getIterator().getType() == Token.FOR_EACH) {
    return newNode(
        Token.FOR_EACH,
        transform(loopNode.getIterator()),
        transform(loopNode.getIteratedObject()),
        transformBlock(loopNode.getBody()));
  }

  // Return the bare minimum to put the AST in a valid state for other types of FOR IN loops
  return newNode(
      Token.FOR,
      transform(loopNode.getIterator()),
      transform(loopNode.getIteratedObject()),
      transformBlock(loopNode.getBody()));
}