private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    // Check if the callee is of type GETPROP before proceeding
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Ensure functionName matches the qualified name before comparing
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext();
        // Check for null to avoid a NullPointerException
        if (target != null) {
          className = target.getString(); // Assuming target is a valid Node instance
        }
      }
    }
  }
  return className;
}