private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Check if the function name is a substring of the qualified name
      if (qualifiedName.contains(functionName)) {
        Node target = callee.getNext();
        if (target != null && Token.isIdentifier(target.getType())) {
          className = target.getString();
        }
      }
    }
  }
  return className;
}