private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Check if the qualified name matches 'goog.require' or 'require'
      if (functionName.equals(qualifiedName) || functionName.equals("require")) {
        Node target = callee.getNext();
        if (target != null && target.getType() == Token.OMBRE_NAME) { // Assuming NAME is the correct type for a string
          className = target.getString();
        }
      }
    }
  }
  return className;
}