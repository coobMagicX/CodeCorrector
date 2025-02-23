private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      return NodeUtil.isExpressionNode(maybeExpr) && !mightBeEventHandlerThatShouldNotBeFolded(maybeExpr);
    }
  }
  
  return false;
}

private boolean mightBeEventHandlerThatShouldNotBeFolded(Node expr) {
  if (expr.getType() == Token.CALL) {
    Node firstChild = expr.getFirstChild();
    if (firstChild != null && firstChild.getType() == Token.GETPROP) {
      Node propertyNode = firstChild.getFirstChild();
      // Ensure to check this condition properly based on your DOM or relevant event handler properties
      if (propertyNode != null && propertyNode.getType() == Token.NAME && "onchange".equals(propertyNode.getString())) {
        return true;
      }
    }
  }
  return false;
}
