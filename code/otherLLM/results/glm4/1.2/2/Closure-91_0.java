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

    // Check for a prototype assignment on the right-hand side of an assignment statement
    if (parent != null && parent.getType() == Token.ASSIGN) {
      Node lhs = parent.getFirstChild();
      Node rhs = lhs.getNext();

      if (n == rhs) { // Now checking if the node is the right-hand side
        if (NodeUtil.isGet(lhs)) {
          if (lhs.getType() == Token.GETPROP &&
              lhs.getLastChild().getString().equals("prototype")) {
            return false; // This function is being lent to a prototype, do not traverse
          }
          Node llhs = lhs.getFirstChild();
          if (llhs != null && llhs.getType() == Token.GETPROP &&
              llhs.getLastChild().getString().equals("prototype")) {
            return false; // This function is being lent to a prototype, do not traverse
          }
        }
      }
    }

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
      return true;
    } else {
      // Only traverse the right side if it's not an assignment to a prototype property or subproperty.
      return !shouldReportThis(n, parent);
    }
  }

  return true;
}