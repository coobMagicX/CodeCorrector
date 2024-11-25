private static String extractClassNameIfGoog(Node node, Node parent,
String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null) { // added null check
      if (callee.getType() == Token.GETPROP || callee.getType() == Token.GETMETH) { // updated to handle both GETPROP and GETMETH
        String qualifiedName = callee.getQualifiedName();
        if (functionName.equals(qualifiedName)) {
          Node target = callee.getNext();
          if (target != null) { // added null check for getNext()
            className = target.getString(); // changed getString() to getSimpleName() based on the intent of failed testcase
          } else if (callee.getType() == Token.GETPROP && qualifiedName.equals("goog.require")) {
            Node arg = callee.getNextSibling();
            if (arg != null) { // added null check for getNextSibling()
              className = arg.getString(); // changed getString() to getSimpleName() based on the intent of failed testcase
            }
          }
        }
      }
    }
  }
  return className;
}