private static boolean isReduceableFunctionExpression(Node n) {
  return hasMultipleGettersInScope(parentOf(n)) && NodeUtil.isFunctionExpression(n);
}

public Node parentOf(Node node) {
  if (node == null || !traversal.hasParent()) {
    return null;
  }
  return traversal.getParent();
}