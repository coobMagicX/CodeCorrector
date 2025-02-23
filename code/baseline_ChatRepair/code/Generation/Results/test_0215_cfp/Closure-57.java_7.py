private static String extractClassNameIfGoog(Node node, Node parent, String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callNode = parent.getFirstChild(); // This should be the function call node
    if (callNode != null && callNode.getType() == Token.GETPROP) {
      String qualifiedName = callNode.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node argumentNode = callNode.getNext(); // The next sibling is the argument to the function
        if (argumentNode != null && argumentNode.isString()) {
          className = argumentNode.getString();
        }
      }
    }
  }
  return className;
}
