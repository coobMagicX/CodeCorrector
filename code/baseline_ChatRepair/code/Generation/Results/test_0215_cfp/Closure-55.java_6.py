private static boolean isReduceableFunctionExpression(Node n) {
  return NodeUtil.isFunctionExpression(n) || NodeUtil.isFunctionDeclaration(n) || n.isArrowFunction();
}
