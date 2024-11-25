public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  // Check for @lends annotation to decide whether to traverse the function node.
  JSDocInfo jsDoc = getFunctionJsDocInfo(n);
  if (jsDoc != null && jsDoc.hasLends()) {
    return false; // Skip traversing functions that are getting lent to a prototype.
  }

  if (n.getType() == Token.FUNCTION) {
    // Don't traverse functions that are constructors or have the @this
    // or @override annotation.
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.isInterface() ||
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
          pType == Token.ASSIGN ||

          // object literal keys
          pType == Token.STRING ||
          pType == Token.NUMBER)) {
      return false;
    }
  }

  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();

    if (n == lhs) {
      // Always traverse the left side of the assignment.
      if (assignLhsChild == null) {
        assignLhsChild = lhs;
      }
    } else {
      // Only traverse the right side if it's not an assignment to a prototype
      // property or subproperty.
      if (NodeUtil.isGet(lhs)) {
        Node llhs = lhs.getFirstChild();
        if (llhs != null && (llhs.getType() == Token.GETPROP &&
                             (llhs.getLastChild().getString().equals("prototype") ||
                              (llhs.getNext() != null &&
                               llhs.getNext().getType() == Token.GETPROP &&
                               llhs.getNext().getLastChild().getString().equals("prototype"))))) {
          return false;
        }
      }
    }
  }

  return true;
}