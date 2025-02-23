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

    // Don't traverse functions unless they resemble a valid this type context
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
      // Always traverse left hand side of assignments
      assignLhsChild = lhs;
    } else {
      // Prevent traversal into prototype chaining assignments
      if (isPrototypeAssignment(lhs)) {
        return false;
      }
    }
  }

  return true;
}

private boolean isPrototypeAssignment(Node node) {
  while (node != null && node.getType() == Token.GETPROP) {
    if ("prototype".equals(node.getLastChild().getString())) {
      return true;
    }
    node = node.getFirstChild();
  }
  return false;
}
