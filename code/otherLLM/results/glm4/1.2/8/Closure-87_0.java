private boolean isFoldableExpressBlock(Node n) {
    if (n.getType() == Token.BLOCK && n.hasOneChild()) {
        Node maybeExpr = n.getFirstChild();

        // Check for the specific edge case described in Issue 291.
        // We need to ensure that we're not dealing with an event handler
        // or other context where the expression's return value is treated differently.
        if (isEventHandlerContext(n)) {
            // If it's an event handler, we don't fold the expression into another expression.
            // This check would typically involve analyzing the parent type and structure,
            // but as we are limited to existing methods, we cannot implement a full analysis.
            // We must assume that if isEventHandlerContext returns true, then folding should not occur.
            return false;
        }

        return NodeUtil.isExpressionNode(maybeExpr);
    }
    return false;
}

private boolean isEventHandlerContext(Node n) {
    while (n != null) {
        switch (n.getType()) {
            case Token.FUNCTION:
                // Check if the parent of a function node is an event handler.
                // This is a simplification and may not cover all cases,
                // but it's the best we can do without additional context analysis.
                Node parent = n.getParent();
                return parent != null && parent.getType() == Token.CALL;
            case Token.CALL:
                // If it's a call, check if it's part of an event handler.
                Node funcName = NodeUtil.getFunctionName(n);
                String functionName = funcName != null ? funcName.toString() : "";
                return functionName.startsWith("on"); // Simplistic check for event handlers
            default:
                break;
        }
        n = n.getParent();
    }
    return false;
}