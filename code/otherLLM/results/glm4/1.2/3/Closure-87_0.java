private boolean isFoldableExpressBlock(Node n) {
    if (n.getType() == Token.BLOCK) {
        if (n.hasOneChild()) {
            Node maybeExpr = n.getFirstChild();
            
            // Check for EXPR_RESULT node and prevent folding into other expressions.
            if (maybeExpr != null && maybeExpr.getType() == Token.EXPR_RESULT) {
                return false;
            }
            
            // Detect the case of event handlers that behave differently when
            // their return value is used in an expression vs. when it's in an EXPR_RESULT.
            // This check is only necessary for methods with an implicit 'this' parameter.
            if (NodeUtil.isEventHandler(maybeExpr) || NodeUtil.isImplicitThisUsage(maybeExpr)) {
                return false;
            }
            
            return NodeUtil.isExpressionNode(maybeExpr);
        }
    }

    return false;
}