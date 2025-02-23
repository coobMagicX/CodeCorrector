private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.isGetProp()) {
      String qualifiedName = callee.getQualifiedName();
      if ("goog.require".equals(qualifiedName) || "goog.provide".equals(qualifiedName) || "goog.module".equals(qualifiedName)) {
        // Skip these cases as they are not meant to extract class names.
        return null;
      }
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getFirstChild();
        if (target != null && target.getNext() != null) {
          Node potentialClassNameNode = target.getNext();
          if (potentialClassNameNode.isString()) {
            return potentialClassNameNode.getString();
          }
        }
      }
    }
  }
  return null;
}
