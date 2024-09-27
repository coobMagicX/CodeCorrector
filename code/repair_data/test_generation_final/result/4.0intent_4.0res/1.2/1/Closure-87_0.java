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

      // We only have to worry about methods with an implicit 'this'
      // param, or this doesn't happen.
      if ((maybeExpr.getType() == Token.EXPR_RESULT &&
           maybeExpr.getFirstChild().getType() == Token.CALL) &&
          maybeExpr.getFirstChild().getFirstChild().getType() == Token.GETPROP &&
          maybeExpr.getFirstChild().getFirstChild().getLastChild().getString().matches("on[a-zA-Z]+")) {
        // Do not fold if the expression is a method call to an event handler (e.g., onchange, onclick)
        return false;
      }

      return NodeUtil.isExpressionNode(maybeExpr);
    }
  }

  return false;
}