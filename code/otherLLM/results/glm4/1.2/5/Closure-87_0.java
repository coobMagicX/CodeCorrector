private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK && n.hasOneChild()) {
    Node maybeExpr = n.getFirstChild();
    
    // Check if the child node is an EXPR_RESULT and ensure it's not a foldable expression.
    // This might happen due to the issue with event handlers as mentioned in the comment.
    boolean isFoldable = !NodeUtil.isExpressionResult(maybeExpr) || NodeUtil.isFoldableExpression(maybeExpr);

    return isFoldable;
  }

  return false;
}