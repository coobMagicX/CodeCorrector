private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK && !n.hasChildren()) {
    return false;
  } else if (n.getType() == Token.BLOCK && n.hasOneChild()) {
    Node maybeExpr = n.getFirstChild();
    if (maybeExpr.getType() == Token.BLOCK || maybeExpr.getType() == Token.INIT) {
      return false;
    }
    return NodeUtil.isExpressionNode(maybeExpr);
  }

  return false;
}