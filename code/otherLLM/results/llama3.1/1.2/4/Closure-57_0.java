private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Input validation: Check if functionName is not empty
      if (!functionName.isEmpty()) {
        if (functionName.equals(qualifiedName)) {
          Node target = callee.getNext();
          if (target != null) {
            className = target.getString();
          }
        }
      }
    } else if (NodeUtil.isExprCall(node)) { // Edge case handling: Check parent and node
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