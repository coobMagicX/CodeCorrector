private static String extractClassNameIfGoog(Node node, Node parent,
String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      // Improved input validation: Check if the qualified name is a string
      String qualifiedName = callee.getQualifiedName();
      if (qualifiedName instanceof String) {
        qualifiedName = (String) qualifiedName;
        // Enhanced expression parsing: Handle different syntax and structures
        if (functionName.equals(qualifiedName)) {
          Node target = callee.getNext();
          if (target != null && target.getType() == Token.STRING_LITERAL) {
            className = target.getString();
          }
        }
      } else {
        // If the qualified name is not a string, try to extract it from the parent node
        NodeUtil.extractQualifiedName(parent);
        qualifiedName = callee.getQualifiedName();
        if (qualifiedName instanceof String && functionName.equals(qualifiedName)) {
          Node target = callee.getNext();
          if (target != null) {
            className = target.getString();
          }
        }
      }
    }
  }
  return className;
}