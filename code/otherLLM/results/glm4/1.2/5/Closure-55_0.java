private static boolean isReduceableFunctionExpression(Node n) {
    if (n instanceof FunctionDeclarationNode || n instanceof FunctionExpressionNode) {
        // Check if it's not a nested function expression or within an object literal property to ensure reducibility
        Node parent = NodeUtil.getParent(n);
        return !((parent != null && (parent instanceof ObjectLiteralPropertyNode || 
            parent instanceof PropertyNode)) ||
                  (parent != null && parent instanceof FunctionDeclarationNode));
    }
    return false;
}