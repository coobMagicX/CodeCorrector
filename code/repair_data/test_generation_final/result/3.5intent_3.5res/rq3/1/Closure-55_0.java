private static boolean isReducibleFunctionExpression(Node n) {
  return NodeUtil.isFunctionExpression(n) && !n.isArrowFunction();
}