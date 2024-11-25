private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName) {
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      // Extract the qualified name from the GETPROP node
      String qualifiedName = callee.getQualifiedName();

      // Check if the function name is a part of the qualified name
      if (qualifiedName.contains(functionName)) {
        // If functionName matches, extract the class name which precedes functionName
        int lastDotIndex = qualifiedName.lastIndexOf('.');
        if (lastDotIndex != -1) {
          className = qualifiedName.substring(0, lastDotIndex);
        }
      }
    }
  }
  return className;
}