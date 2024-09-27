private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      
      if (maybeExpr.getType() == Token.NEW) {
        maybeExpr = tryFoldStandardConstructors(maybeExpr);
      }

      return NodeUtil.isExpressionNode(maybeExpr);
    }
  }

  return false;
}

private Node tryFoldStandardConstructors(Node n) {
  Preconditions.checkState(n.getType() == Token.NEW);

  // If name normalization has been run then we know that
  // new Object() does in fact refer to what we think it is
  // and not some custom-defined Object().
  if (isASTNormalized()) {
    if (n.getFirstChild().getType() == Token.NAME) {
      String className = n.getFirstChild().getString();
      if (STANDARD_OBJECT_CONSTRUCTORS.contains(className)) {
          n.setType(Token.CALL);
          reportCodeChange();
      }
    }
  }

  return n;
}