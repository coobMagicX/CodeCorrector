private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Check if the function name is an expression and extract its string representation
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext();
        while (target != null) {
          // If the next node is a GETPROP, it's part of a qualified name
          if (target.getType() == Token.GETPROP) {
            qualifiedName += "." + target.getString(); // Append to the qualified name
          } else if (target.getType() == Token.STRING) { // STRING indicates an identifier or literal
            className = qualifiedName + "." + target.getString();
            break; // Stop searching once a valid class name is found
          }
          target = target.getNext(); // Move to the next node
        }
      }
    }
  }
  return className;
}