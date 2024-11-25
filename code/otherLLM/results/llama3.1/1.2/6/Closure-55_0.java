private static boolean isReduceableFunctionExpression(Node n) {
  return NodeUtil.isFunctionExpression(n) && n.getExprs().size() == 1;
}