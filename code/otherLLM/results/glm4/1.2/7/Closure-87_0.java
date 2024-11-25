private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
      
      // We need to detect event handlers and other nodes that have special behavior when their return value is used.
      // For example, EXPR_RESULT should not be folded into other expressions due to the potential for different behaviors
      // in different contexts.

      if (NodeUtil.isExpressionNode(maybeExpr) && maybeExpr.getType() != Token.EXPR_RESULT) {
        // If it's not an EXPR_RESULT and is an expression node, then check further conditions.
        return NodeUtil.isFoldableExpression(maybeExpr); // Assuming this method exists to check foldability of the expression
      }
    }
  }

  return false;
}