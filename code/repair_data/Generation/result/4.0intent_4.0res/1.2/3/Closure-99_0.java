public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.hasThisType() ||
         jsDoc.isOverride())) {
      return false;
    }

    // Allow traversal of JavaScript functions annotated as interfaces
    if (jsDoc != null && jsDoc.isInterface()) {
      return true;
    }

    // Check if the function is a method (a property of an object)
    if (parent != null && parent.getType() == Token.GETPROP) {
      return true; // Always traverse methods of objects
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
          lhs.getLastChild().getString().equals("prototype")) {
        return false;
      }
      if (lhs.getQualifiedName() != null && lhs.getQualifiedName().contains(".prototype.")) {
        return false;
      }
    }
  }

  return true;
}