private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        // Check if the next sibling of GETPROP is a CALL node
        Node callNode = callee.getNextSibling();
        if (callNode != null && callNode.getType() == Token.CALL) {
          className = extractClassNameIfGoog(callNode, parent, functionName);
        }
      }
    }
  }
  return className;
}