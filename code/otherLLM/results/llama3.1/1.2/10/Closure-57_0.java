private static String extractClassNameIfGoog(Node node, Node parent,
String functionName){
  String className = null;
  
  // Improved string comparison logic: use equalsIgnoreCase for case-insensitive comparison
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName().trim(); // Trim quotes and spaces
      if (functionName.equalsIgnoreCase(qualifiedName)) { 
        Node target = callee.getNext();
        if (target != null) {
          className = target.getString();
        }
      }
    } else if (parent.getType() == Token.NAME && functionName.equals(parent.getString())) {
      // Handle edge case for functionName parameter: allow matching with parent node's name
      className = parent.getString();
    }
  }
  
  return className;
}