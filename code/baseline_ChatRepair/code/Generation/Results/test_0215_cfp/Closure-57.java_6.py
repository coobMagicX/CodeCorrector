private static String extractClassNameIfGoog(Node node, Node parent, String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callNode = parent.getFirstChild();
    if (callNode != null && callNode.getType() == Token.GETPROP) {
      String qualifiedName = callNode.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node getpropNode = callNode.getFirstChild(); // Get the last child of the GETPROP node that provides the class name
        if (getpropNode != null) {
          className = getpropNode.getQualifiedName();
        }
      }
    }
  }
  return className;
}
