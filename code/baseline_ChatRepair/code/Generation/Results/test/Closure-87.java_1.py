private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();

      // Check whether this block's only child is an expression result node.
      if (maybeExpr.getType() == Token.EXPR_RESULT) {
        // Ensure the parent node's type requires block form (e.g., IF, SWITCH).
        Node parent = n.getParent();
        if (parent != null) {
          switch (parent.getType()) {
            case Token.IF:
            case Token.FOR:
            case Token.WHILE:
            case Token.DO:
              return false;
            default:
              break;
          }
        }

        return true;
      }
    }
  }

  return false;
}
