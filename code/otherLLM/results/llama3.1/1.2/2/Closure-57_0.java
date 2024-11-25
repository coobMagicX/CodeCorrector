private static String extractClassNameIfGoog(Node node, Node parent,
String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName().trim(); // Add trim() to remove leading/trailing whitespace
      functionName = functionName.trim(); // Add trim() to remove leading/trailing whitespace
      if (functionName.equals(qualifiedName)) { // Compare trimmed function name
        Node target = callee.getNext();
        if (target != null) {
          className = target.getString();
        } else {
          logger.error("Expected target node not found for functionName: " + functionName); // Add logging statement
        }
      } else {
        logger.error("FunctionName '" + functionName + "' does not match expected function name"); // Add logging statement
      }
    }
  }
  return className;
}