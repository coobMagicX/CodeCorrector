private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = parent.getFirstChild().getFirstChild(); // Adjusted to access the callee of the call
    if (callee != null && callee.isGetProp()) { // Instead of getType == Token.GETPROP, use isGetProp() for clearer semantics
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node argumentNode = parent.getFirstChild().getNext(); // Argument should be the next sibling of the call node itself
        if (argumentNode != null && argumentNode.getType() == Token.STRING) { // Check if the argument node is of type STRING
          className = argumentNode.getString();
        }
      }
    }
  }
  return className;
}
