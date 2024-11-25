/**
 * Processes a 'for each' loop by transforming its AST nodes and returning 
 * a new node with the correct structure.
 */
Node processForInLoop(ForInLoop loopNode) {

    // Check if the loopNode is indeed a ForInLoop (for each) type
    if (!(loopNode instanceof ForInLoop)) {
        throw new RuntimeException("Invalid 'for each' loop node encountered");
    }

    // Return the bare minimum to put the AST in a valid state.
  return newNode(
      Token.FOR,
      transform(loopNode.getIterator()),
      transform(loopNode.getIteratedObject()),
      processBlock(transform(loopNode.getBody())));
}