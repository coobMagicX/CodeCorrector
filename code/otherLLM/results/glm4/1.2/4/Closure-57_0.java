private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Check for correct usage: function call should not have arguments
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext();
        // Ensure that the next node is an IDENT token to handle the class name correctly
        if (target != null && target.getType() == Token.IDENT) {
          className = target.getString();
        }
      }
    }
  }
  return className;
}