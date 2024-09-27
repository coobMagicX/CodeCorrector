private static boolean isReduceableFunctionExpression(Node n) {
    return NodeUtil.isFunctionExpression(n) || NodeUtil.isGetterMethod(n);
}