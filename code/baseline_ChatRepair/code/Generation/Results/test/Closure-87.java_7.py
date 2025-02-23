private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();

      // Detailed EXPR_RESULT check to avoid folding certain expressions into statements
      // Achieved by ensuring the node is not one with a critical side effect.
      return (maybeExpr.getType() == Token.EXPR_RESULT &&
              !NodeUtil.mayHaveSideEffects(maybeExpr.getFirstChild()));
    }
  }
  return false;
}
