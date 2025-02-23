public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null) {
      if ((jsDoc.isConstructor() || jsDoc.isInterface()) &&
          (jsDoc.hasThisType() || jsDoc.isOverride())) {
        // Constructors and interfaces are exceptions, they should be traversed
        // unless it is purely a @this annotation without being a constructor or interface.
      } else if (jsDoc.hasThisType() || jsDoc.isOverride()) {
        return false;
      }
    }

    int pType = parent.getType();
    if (!(pType == Token.BLOCK ||
          pType == Token.SCRIPT ||
          pType == Token.NAME ||
          pType == Token.ASSIGN ||
          pType == Token.STRING ||
          pType == Token.NUMBER)) {
      return false;
    }
  }

  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();
    if (n == lhs) {
      if (assignLhsChild == null) {
        assignLhsChild = lhs;
      }
    } else {
      if (NodeUtil.isGet(lhs)) {
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
  }

  return true;
}
