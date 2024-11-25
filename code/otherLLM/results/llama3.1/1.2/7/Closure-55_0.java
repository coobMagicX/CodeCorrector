private static boolean isReduceableFunctionExpression(Node n) {
  // Check if the node represents a function expression, considering constructor functions with multiple getter properties.
  return (NodeUtil.isFunctionExpression(n) && 
          n.hasChildren() && 
          n.getChildren().stream()
          .anyMatch(child -> child.getType() == Token.GETTER));
}