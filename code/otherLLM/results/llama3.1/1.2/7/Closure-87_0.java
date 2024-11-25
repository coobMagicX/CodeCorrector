private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK) {
    if (n.hasOneChild()) {
      Node maybeExpr = n.getFirstChild();
        // IE has a bug where event handlers behave differently when
        // their return value is used vs. when their return value is in
        // an EXPR_RESULT. It's pretty freaking weird. See:
        // http://code.google.com/p/closure-compiler/issues/detail?id=291
        // We try to detect this case, and not fold EXPR_RESULTs
        // into other expressions.

      // Check if the node is an event handler with an implicit 'this' param
      boolean hasImplicitThis = NodeUtil.isEventHandlerWithImplicitThis(maybeExpr);
      
      // We only have to worry about methods with an implicit 'this'
      // param, or this doesn't happen.
      if (hasImplicitThis) {
        return false;
      }

      // Check if the expression is in an EXPR_RESULT
      boolean isInExprResult = NodeUtil.isInExprResult(maybeExpr);
      
      // If it's not in an EXPR_RESULT and it's not an event handler with implicit 'this', 
      // then we can fold the block into a single expression
      return !isInExprResult && !hasImplicitThis && NodeUtil.isExpressionNode(maybeExpr);
    }
  }

  return false;
}