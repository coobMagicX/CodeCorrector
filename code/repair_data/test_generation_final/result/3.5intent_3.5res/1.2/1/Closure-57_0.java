private static String extractClassNameIfGoog(Node node, Node parent, String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      if (qualifiedName != null && (qualifiedName.startsWith("'") || qualifiedName.startsWith("\"")) 
          && (qualifiedName.endsWith("'") || qualifiedName.endsWith("\""))) {
        qualifiedName = qualifiedName.substring(1, qualifiedName.length() - 1);
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