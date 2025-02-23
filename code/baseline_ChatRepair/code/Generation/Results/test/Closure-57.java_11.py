private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.isGetProp()) {
      String qualifiedName = callee.getQualifiedName();
      // Only extract class name if functionName is alignment with a method that offers class name
      if (qualifiedName != null && qualifiedName.equals(functionName)) {
        Node getPropNode = callee.getFirstChild();
        if (getPropNode != null && getPropNode.getNext() != null) {
          Node target = getPropNode.getNext();
          if (target.isString()) {
            className = target.getString();
          }
        }
      }
    }
  }
  return className;
}
