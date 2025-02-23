private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
        return NodeUtil.isExpressionNode(maybeExpr) && !mightBeEventHandlerThatShouldNotBeFolded(maybeExpr);
    }
  }
  return false;
}

/**
 * Check if the node is potentially an event handler that should not be folded
 * due to IE behavior noted in the issue 291 of Google Closure Compiler.
 */
private boolean mightBeEventHandlerThatShouldNotBeFolded(Node expr) {
  if (expr.isCall()) {
    Node getPropNode = expr.getFirstChild();
    if (getPropNode != null && getPropNode.isGetProp()) {
      Node nameNode = getPropNode.getFirstChild();
      if (nameNode != null && "onchange".equals(nameNode.getString())) {
        return true;
      }
    }
  }
  return false;
}
