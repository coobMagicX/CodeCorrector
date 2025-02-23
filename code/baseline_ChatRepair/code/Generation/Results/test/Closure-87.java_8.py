private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      // Ensure the block that contains exactly one child node is foldable only if it is an expression node.
      if (maybeExpr.getType() == Token.EXPR_RESULT) {
        // Check the contents of the EXPR_RESULT for potential side effects.
        Node exprResultChild = maybeExpr.getFirstChild();

        // Given the specificity of the issue with IE described in the comment,
        // we make sure to not fold EXPR_RESULT that might involve function calls
        // or other complex expressions which may differ when evaluated directly 
        // versus when their result is used or discarded in an expression context.
        if (exprResultChild != null) {
          // Avoid folding if may have side-effects or is part of an assignment or a call.
          if (NodeUtil.mayHaveSideEffects(exprResultChild)) {
            return false;
          }
        }
        return true;
      }
    }
  }
  return false;
}
