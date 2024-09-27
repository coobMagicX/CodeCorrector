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
      
      if (NodeUtil.isExpressionNode(maybeExpr)) {
        Node expr = maybeExpr;
        
        // Check if the expression is a call to an event handler
        if (expr.getType() == Token.CALL) {
          Node callee = expr.getFirstChild();
          
          // Check if the callee is an event handler property
          if (callee.getType() == Token.GETPROP && callee.getLastChild().getString().endsWith("onchange")) {
            // Check if the event handler call is inside an if statement
            Node parent = n.getParent();
            if (parent != null && parent.getType() == Token.IF) {
              return false; // Do not fold expression block with event handler call inside if statement
            }
          }
        }
        
        return true; // Foldable expression block
      }
    }
  }
  
  return false;
}