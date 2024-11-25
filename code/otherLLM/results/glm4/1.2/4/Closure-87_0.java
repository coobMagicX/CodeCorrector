private boolean isFoldableExpressBlock(Node n) {
    if (n.getType() == Token.BLOCK) {
        if (n.hasOneChild()) {
            Node maybeExpr = n.getFirstChild();

            // Check if the first child is an event handler, which would require careful handling due to the implicit 'this' parameter.
            boolean isEventHandler = NodeUtil.isEventHandler(maybeExpr);
            
            // We need to handle EXPR_RESULTs properly, especially in the context of event handlers and methods with an implicit 'this' parameter.
            if (NodeUtil.isExpressionResult(maybeExpr)) {
                // If it's an expression result within an event handler or a method with an implicit 'this', do not fold it.
                if (isEventHandler || NodeUtil.hasImplicitThisParam(maybeExpr.getParent())) {
                    return false;
                }
            }

            return NodeUtil.isExpressionNode(maybeExpr);
        }
    }

    return false;
}