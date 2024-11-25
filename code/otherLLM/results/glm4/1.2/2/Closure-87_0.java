private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();

      // Check if the child node is an expression node and not of type EXPR_RESULT.
      if (NodeUtil.isExpressionNode(maybeExpr) && !(maybeExpr.getType() == Token.EXPR_RESULT)) {
        return true; // The block can be folded into an expression.
      }
    }
  }

  return false;
}