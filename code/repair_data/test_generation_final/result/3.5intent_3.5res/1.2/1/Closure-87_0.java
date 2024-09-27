private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      if (maybeExpr.getType() == Token.EXPR_RESULT) {
        maybeExpr = maybeExpr.getFirstChild();
      }
      if (maybeExpr.getType() == Token.CALL || maybeExpr.getType() == Token.NEW) {
        return true;
      }
      return NodeUtil.isExpressionNode(maybeExpr);
    }
  }

  return false;
}