private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      // Check for the special case of event handlers where their return value
      // might not be foldable into an expression.
      if (NodeUtil.isEventHandler(maybeExpr)) {
        // If it's an event handler, we need to ensure that its return value
        // is not being folded into another expression.
        return false;
      }
      // If the node is not an event handler or doesn't have a problem, check if it's foldable.
      return NodeUtil.isExpressionNode(maybeExpr);
    }
  }

  return false;
}