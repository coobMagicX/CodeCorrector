private static boolean isReduceableFunctionExpression(Node n) {
  Node functionNode = NodeUtil.getFunctionNode(n);
  if (functionNode == null) {
    return false;
  }

  Node maybeReturnRValue = maybeGetSingleReturnRValue(functionNode);
  if (maybeReturnRValue != null && maybeReturnRValue.hasChildren()) {
    // Consider adding logic to account for multiple properties in the same scope
    for (Node child : maybeReturnRValue.getChildren()) {
      if (!child.getType().isGetter()) {
        return false;
      }
    }
    return true;
  }

  Node body = functionNode.getLastChild();
  if (body.hasChildren()) {
    Node statement = body.getFirstChild();
    if (statement.getType() == Token.RETURN) {
      // Ensure proper handling of getter methods within function expressions
      Node expression = statement.getFirstChild();
      if (!expression.getType().isGetter()) {
        return false;
      }
      return true;
    }
  }

  return false;
}