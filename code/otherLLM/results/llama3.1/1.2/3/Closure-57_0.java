private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && (callee.getType() == Token.GETPROP || callee.getType() == Token.GETPROP_VAR) ) {
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext();
        if (target != null) {
          className = target.getString();
        }
      }
    } else if (NodeUtil.isExprCall(node)) { // added this condition to handle different parameters
      Node calleeNode = node.getFirstChild();
      if (calleeNode != null && calleeNode.getType() == Token.GETPROP) {
        String qualifiedName = calleeNode.getQualifiedName();
        if (functionName.equals(qualifiedName)) {
          Node target = calleeNode.getNext();
          if (target != null) {
            className = target.getString();
          }
        }
      }
    }
  }
  return className;
}