private static boolean isReduceableFunctionExpression(Node n) {
    // Check if the node is a FunctionExpression and has 'constructor' property set to true.
    return NodeUtil.isFunctionExpression(n) && n.hasProperty("kind") && "Constructor".equals(n.getProperty("kind"));
}