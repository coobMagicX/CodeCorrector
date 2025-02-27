public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.hasThisType() ||
         jsDoc.isOverride())) {
      return false;
    }

    int pType = parent.getType();
    if (!(pType == Token.BLOCK ||
          pType == Token.SCRIPT ||
          pType == Token.NAME ||
          pType == Token.ASSIGN)) {
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
      if (lhs.getType() == Token.GETPROP &&
          "prototype".equals(lhs.getLastChild().getString())) {
          return false;
      }
      if (lhs.getQualifiedName() != null && lhs.getQualifiedName().contains(".prototype.")) {
          return false;
      }
    }
  }

  return true;
}
