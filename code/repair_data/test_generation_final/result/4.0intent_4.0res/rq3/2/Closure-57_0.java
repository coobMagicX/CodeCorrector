private static String extractClassNameIfGoog(Node node, Node parent, String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = parent.getFirstChild();
    if (callee != null && callee.isGetProp()) {
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getFirstChild();
        if (target != null) {
          className = target.getString();
        }
      }
    }
  }
  return className;
}