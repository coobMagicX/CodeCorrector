private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
        // Additional check for EXPR_RESULT
        if (maybeExpr.getType() == Token.EXPR_RESULT) {
            Node childOfExpr = maybeExpr.getFirstChild();
            // Return false to prevent folding in case of a non-trivial expression
            // where side effects matter, such as function calls or assignments.
            if (childOfExpr != null && (childOfExpr.isFunction() || childOfExpr.isAssign() ||
                NodeUtil.mayHaveSideEffects(childOfExpr))) {
                return false;
            }
        }

        // Original condition to check for an Expression node
        if (NodeUtil.isExpressionNode(maybeExpr)) {
            return true;
        }
    }
  }

  return false;
}
