Node processForInLoop(ForInLoop loopNode) {
  // Check if the loopNode is a "for each" loop
  if (loopNode.isForEach()) {
    // Handle "unsupported language extension: for each" error
    parseError("Unsupported language extension: for each");
    return null;
  }

  // Return the bare minimum to put the AST in a valid state.
  return newNode(
    Token.FOR,
    transform(loopNode.getIterator()),
    transform(loopNode.getIteratedObject()),
    transformBlock(loopNode.getBody())
  );
}