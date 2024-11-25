private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      // Check if the function call is actually a method call by looking for parentheses
      boolean hasParentheses = parent.getNextSibling().getType() == Token.LPAREN;
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName)) {
        Node target = callee.getNext();
        if (target != null) {
          // Check if the next node is a right parenthesis which indicates end of method call
          boolean isMethodCall = hasParentheses && parent.getNextSibling().getNextSibling().getType() == Token.RPAREN;
          if (isMethodCall) {
            className = target.getString();
          }
        }
      }
    }
  }
  return className;
}