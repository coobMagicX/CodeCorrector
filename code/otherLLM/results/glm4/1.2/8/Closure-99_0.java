public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  // Check for functions that are not constructors or do not have @this or @override annotation.
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.hasThisType() ||
         jsDoc.isOverride())) {
      return false;
    }

    int pType = parent.getType();
    // Check if the function is not in a block, script, or a name context.
    if (!(pType == Token.BLOCK ||
          pType == Token.SCRIPT ||
          pType == Token.NAME ||
          pType == Token.ASSIGN)) {
      return false;
    }
  }

  // If parent node is an assignment, check if the left-hand side is not an assignment to a prototype property.
  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();

    if (n == lhs) {
      // Always traverse the left side of the assignment.
      if (assignLhsChild == null) {
        assignLhsChild = lhs;
      }
    } else {
      // Only traverse the right side if it's not an assignment to a prototype property or subproperty.
      if (lhs.getType() == Token.GETPROP) {
        Node getter = lhs.getLastChild();
        if ("prototype".equals(getter.getString())) {
          return false;
        }
      }
      if (lhs.getQualifiedName() != null && lhs.getQualifiedName().contains(".prototype.")) {
        return false;
      }
    }
  }

  return true;
}