private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext();
        // Check if the next node is a function call to ensure it's not just an identifier
        if (target != null && (target.getType() == Token.FUNCALL || target.getType() == Token.IDENTIFIER)) {
          className = target.getString(); // Assuming that the class name could be either after GETPROP or IDENTIFIER
        }
      }
    } else if (callee != null && callee.getType() == Token.IDENTIFIER) {
      // Handle the case where the parent is a function call directly with an identifier as the parameter
      className = callee.getString();
    }
  }
  return className;
}