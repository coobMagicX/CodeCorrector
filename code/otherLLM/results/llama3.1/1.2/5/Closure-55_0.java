private static boolean isReduceableFunctionExpression(Node n) {
  if (NodeUtil.isFunctionExpression(n)) {
    // Check if the function expression has any property getters
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getKind() == Node.Kind.GETTER_EXPRESSION) {
        return false;
      }
    }
  }
  return true;
}