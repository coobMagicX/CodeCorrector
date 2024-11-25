private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Validate functionName format
      if (!qualifiedName.startsWith("goog.require('") && !qualifiedName.startsWith("goog.require(`")) {
        return "";
      }
      
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext();
        if (target != null) {
          className = target.getString();
        }
      }
    }
  }
  return className;
}