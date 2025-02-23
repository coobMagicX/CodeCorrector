private static boolean isReduceableFunctionExpression(Node n) {
  return NodeUtil.isFunctionExpression(n) && !n.isConstructor() && !n.isCall();
}
