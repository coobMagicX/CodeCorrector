public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.isInterface() ||
         jsDoc.hasThisType() ||
         jsDoc.isOverride())) {
      return false;
    }

    int pType = parent.getType();
    // Check for valid parent types to determine if the function can have a @this annotation
    if (!(pType == Token.BLOCK ||
          pType == Token.SCRIPT ||
          pType == Token.NAME ||
          pType == Token.ASSIGN ||
          pType == Token.OBJECTLIT)) {
      return false;
    }

    // Check for functions that are getting lent to a prototype.
    Node rhs = n.getParent().getFirstChild();
    if (rhs.getType() == Token.GETPROP) {
      Node grandParent = rhs.getParent();
      if (grandParent.getType() == Token.ASSIGN &&
          grandParent.getLastChild().getType() == Token.GETPROP &&
          grandParent.getLastChild().getLastChild().getString().equals("prototype")) {
        return false;
      }
    }
  }

  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();

    if (n == lhs) {
      assignLhsChild = lhs;
    } else {
      // Only traverse the right side if it's not an assignment to a prototype
      // property or subproperty.
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