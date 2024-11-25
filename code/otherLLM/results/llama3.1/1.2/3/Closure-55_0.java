private static boolean isReduceableFunctionExpression(Node n) {
  if (NodeUtil.isFunctionExpression(n)) {
    // Check if the function expression has multiple statements or getter methods
    return !hasSingleStatementOrGetterMethods(n);
  }
  return false;
}

// Helper method to check for single statement or getter methods in a node
private static boolean hasSingleStatementOrGetterMethods(Node n) {
  if (NodeUtil.hasSingleStatementOrGetterMethod(n)) {
    // If the node has a single statement or getter method, it's not reduceable
    return true;
  }
  
  // Recursively check sub-nodes
  for (Node child : n.getChildren()) {
    if (hasSingleStatementOrGetterMethods(child)) {
      return true;
    }
  }

  return false;
}