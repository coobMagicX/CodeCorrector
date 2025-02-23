public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.isInterface() ||
         (jsDoc.hasThisType() && !jsDoc.isConstructor()) ||
         jsDoc.isOverride())) {
      return false;
    }
    
    // Allow traversal on constructors to handle the 'this' keyword correctly.
    if (jsDoc != null && jsDoc.isConstructor()) {
      return true; // Assume correct use of 'this' within constructors.
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
    if (lhs != null && NodeUtil.isGet(lhs)) {
      if (lhs.getLastChild().getString().equals("prototype")) {
        return false; // Avoid traversing assignments to prototype properties.
      }
    }
  }

  return true;
}
