private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName) {
  String className = null;
  if (NodeUtil.isExprCall(node)) { // Changed from parent to node
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext(); // The next sibling of the GETPROP
        Node parentOfTarget = target.getParent(); // Get the parent of the target

        // Ensure that the parent of the target is the same as the node passed to the method
        if (parentOfTarget != null && parentOfTarget.equals(node)) {
          className = target.getString();
        }
      }
    }
  }
  return className;
}