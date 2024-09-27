private static boolean isReduceableFunctionExpression(Node n) {
    // Check if the node is a function expression
    if (NodeUtil.isFunctionExpression(n)) {
        return true;
    }
    // Additional checks for getter and setter methods that might be overlooked
    return n.isGetterDef() || n.isSetterDef();
}