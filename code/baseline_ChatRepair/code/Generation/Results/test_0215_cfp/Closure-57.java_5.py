private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        // Ensure this is not a module management function call like goog.require
        if (!"goog.require".equals(qualifiedName) && !"goog.provide".equals(qualifiedName) && !"goog.module".equals(qualifiedName)) {
          Node target = callee.getFirstChild();
          if (target != null && target.getNext() != null) {
            Node possibleClassNameNode = target.getNext();
            if (possibleClassNameNode.isString()) {
              className = possibleClassNameNode.getString();
            } else {
              // In failing cases, appropriate null or default handling may be inserted here
            }
          }
        }
      }
    }
  }
  return className;
}
