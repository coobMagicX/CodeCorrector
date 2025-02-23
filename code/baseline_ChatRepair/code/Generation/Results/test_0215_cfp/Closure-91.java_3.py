public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null) {
      // Do not traverse constructor functions annotated with @this or @override without proper context.
      if (jsDoc.isConstructor() && (
          jsDoc.hasThisType() ||
          jsDoc.isOverride() ||
          jsDoc.hasFileOverview())) { // adding additional check for global context misuse
        return false;
      }
    }

    // Ensuring proper traversal context for annotated functions.
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

  // Ensuring left-hand side of assignments are always traversed
  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    if (n == lhs && NodeUtil.isAssignmentOp(parent)) {
      if (assignLhsChild == null) {
        assignLhsChild = lhs;
      }
    } else {
      // Prevent traversal of function assignments to prototype properties
      if (NodeUtil.isGet(lhs) && NodeUtil.isPrototypeProperty(lhs)) {
        return false;
      }
    }
  }

  return true;
}
