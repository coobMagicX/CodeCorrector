public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  if (n.getType() == Token.FUNCTION) {
    // Don't traverse functions that are constructors or have the @this
    // or @override annotation.
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.hasThisType() ||
         jsDoc.isOverride())) {
      return false;
    }

    // Don't traverse functions unless they would normally
    // be able to have a @this annotation associated with them.
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
      // Always traverse the left side of the assignment.
      assignLhsChild = lhs;
    } else {
      // Traversal on RHS should not be skipped if we're assigning to a prototype property directly.
      if (lhs.getType() == Token.GETPROP &&
          "prototype".equals(lhs.getLastChild().getString())) {
        // Also allow traversal if the assignment is to a prototype's subproperty directly by checking if the parent of lhs
        if (lhs.getFirstChild().getType() != Token.GETPROP) {
          return false;
        }
      }
    }
  }

  return true;
}
