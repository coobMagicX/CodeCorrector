private boolean isFoldableExpressBlock(Node n) {
  // Ensure that the node is a block containing only one child.
  if (n.getType() == Token.BLOCK && n.hasOneChild()) {
    Node child = n.getFirstChild();

    // Ensure we are handling an EXPR_RESULT node
    if (child.getType() == Token.EXPR_RESULT) {
      Node expr = child.getFirstChild();

      // If the expression is a call (could affect external state or depend on its context in a subtle way),
      // do not fold it into other blocks especially when it might be influenced by browser behavior quirks
      if (expr.getType() == Token.CALL) {
        return false; // Prevent folding if it's a function call
      }
    }
    
    // If none of the special conditions were met, allow folding if it's a standalone expression node
    return NodeUtil.isExpressionNode(child);
  }

  return false;
}
