private static boolean isReduceableFunctionExpression(Node n) {
    // Ensure to check if the node is a FunctionExpression or not
    if (n instanceof FunctionExpression) {
        return true; // Assuming that all function expressions are reducible for this example
    }
    return false;
}