private static boolean isReduceableFunctionExpression(Node n) {
  // Check if the node is a function expression
  if (NodeUtil.isFunctionExpression(n)) {
    // Check if the node is a getter or a setter
    if (n.isGetterDef() || n.isSetterDef()) {
      return true;
    }
  }
  
  return false;
}