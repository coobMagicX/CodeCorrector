private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && (callee.getType() == Token.GETPROP || callee.getQualifiedName().equals(functionName))) {
      String qualifiedName = callee.getQualifiedName();
      if (functionName.equals(qualifiedName) || functionName.equals(callee.getString())) {
        Node target = callee.getNext();
        if (target != null) {
          className = target.getString();
        }
      }
    } else if (NodeUtil.isExprCall(parent) && callee.getType() == Token.GETFNEXPR) {
      String funcName = callee.getQualifiedName();
      if ("goog.require".equals(funcName)) {
        Node arg = callee.getNext();
        if (arg != null) {
          className = arg.getString();
        }
      } else if ("foo".equals(funcName)) {
        Node arg = callee.getNext();
        if (arg != null && arg.getType() == Token.STRING) {
          className = arg.getString();
        }
      }
    }
  }
  return className;
}