private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      
      // Check if the child node is an EXPR_RESULT and not fold it into another expression.
      if (maybeExpr.getType() == Token.EXPR_RESULT && !NodeUtil.isExpressionNode(maybeExpr)) {
        return false; // Do not fold EXPR_RESULTs into expressions
      }
      
      return NodeUtil.isExpressionNode(maybeExpr);
    }
  }

  return false;
}