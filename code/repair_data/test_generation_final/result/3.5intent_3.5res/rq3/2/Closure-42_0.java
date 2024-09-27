Node processForInLoop(ForInLoop loopNode) {
  // Return the bare minimum to put the AST in a valid state.
  return newNode(
      Token.FOR_OF, // Use Token.FOR_OF instead of Token.FOR
      transform(loopNode.getIterator()),
      transform(loopNode.getIteratedObject()),
      transformBlock(loopNode.getBody()));
}