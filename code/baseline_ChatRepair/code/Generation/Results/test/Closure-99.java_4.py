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
      // Refine checking to make sure all prototype-related assignments are filtered
      if (isPrototypeAssignment(lhs)) {
          return false;
      }
    }
  }

  return true;
}

/**
 * Check if the node represents an assignment to a prototype or its properties.
 * This involves checking if any part of its hierarchy includes "prototype".
 */
private boolean isPrototypeAssignment(Node node) {
  while (node != null) {
    if (node.getType() == Token.GETPROP) {
      String propName = node.getString();
      if ("prototype".equals(propName)) {
        return true;
      }
    }
    node = node.getFirstChild();
  }
  return false;
}
