private boolean isFoldableExpressBlock(Node n) {
  if (n.isBlock()) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      return NodeUtil.isExpressionNode(maybeExpr) && !mightBeEventHandlerThatShouldNotBeFolded(maybeExpr);
    }
  }
  
  return false;
}

private boolean mightBeEventHandlerThatShouldNotBeFolded(Node expr) {
  if (expr.getType() == Token.CALL) { // Assuming `Token.CALL` is the correct way to identify call expressions in your context
    Node getPropNode = expr.getFirstChild();
    if (getPropNode != null && getPropNode.getType() == Token.GETPROP) {
      Node nameNode = getPropNode.getFirstChild();
      if (nameNode != null && nameNode.isString() && "onchange".equals(nameNode.getString())) {
        return true;
      }
    }
  }
  return false;
}
