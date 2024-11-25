private static boolean isReduceableFunctionExpression(Node n) {
  if (n instanceof FunctionExpression && !isGetterOnlyFunction(n)) {
    return false; // Don't reduce if it has multiple getter methods
  }
  return NodeUtil.isFunctionExpression(n);
}

public boolean isGetterOnlyFunction(Node n) {
  // Assuming this method exists and returns true if the function expression 
  // has only getter methods in its prototype.
  // Method implementation not shown, assuming correctness
}