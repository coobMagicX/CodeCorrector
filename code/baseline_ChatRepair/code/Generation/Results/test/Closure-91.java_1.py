public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null) {
      if (jsDoc.isConstructor() || jsDoc.isInterface()) {
        // Always traverse constructor and interface functions.
        return true;
      } else if (jsDoc.hasThisType() || jsDoc.isOverride()) {
        // Skip functions if they have @this or @override annotations (except constructors/interfaces).
        return false;
      }
    }
  }

  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();

    if (n == rhs && NodeUtil.isGet(lhs)) {
      // Avoid traversing the right-hand side of an assignment if it assigns to a prototype.
      if (lhs.getType() == Token.GETPROP && 
          lhs.getLastChild().getString().equals("prototype")) {
        return false;
      }
      Node llhs = lhs.getFirstChild();
      if (llhs.getType() == Token.GETPROP &&
          llhs.getLastChild().getString().equals("prototype")) {
        return false;
      }
    }
  }

  return true;
}
