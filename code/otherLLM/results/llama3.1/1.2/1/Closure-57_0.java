private static String extractClassNameIfGoog(Node node, Node parent,
    String functionName){
  String className = null;
  if (NodeUtil.isExprCall(parent)) {
    Node callee = node.getFirstChild();
    if (callee != null && callee.getType() == Token.GETPROP) {
      String qualifiedName = callee.getQualifiedName();
      // Improve function name matching
      if (qualifiedName.startsWith(functionName + ".")) {
        Node target = callee.getNext();
        if (target != null) {
          className = target.getString();
        }
      } else if (functionName.equals(qualifiedName)) { 
        // Check for other cases of goog.require()
        Node target = safeNext(callee);
        if (target != null && endsWithPrototype(target)) {
          className = functionName;
        } else {
          target = safeNext(target);
          if (target != null) {
            className = target.getString();
          }
        }
      }
    }
  }
  return className;
}