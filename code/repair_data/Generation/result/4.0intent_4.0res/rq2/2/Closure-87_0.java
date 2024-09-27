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

            // Check if the child node is an expression node
            if (NodeUtil.isExpressionNode(maybeExpr)) {
                // Further check if the expression node is not an EXPR_RESULT
                // to handle the IE specific bug mentioned above
                if (maybeExpr.getType() != Token.EXPR_RESULT) {
                    // Additionally, ensure this is not an event handler case
                    // which behaves differently as per issue 291
                    return !maybeExpr.isEventHandler();
                }
            }
        }
    }
    return false;
}