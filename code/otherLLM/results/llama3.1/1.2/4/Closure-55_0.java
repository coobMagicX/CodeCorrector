private static boolean isReduceableFunctionExpression(Node n) {
  if (NodeUtil.isFunctionExpression(n)) {
    // Check if the Node has a body and at least one child
    return !n.getBody().isEmpty() && !n.getChildren().isEmpty();
  }
  return false;
}