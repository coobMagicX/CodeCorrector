public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = NodeUtil.getFunctionJSDocInfo(n);
    if (jsDoc != null) {
      if (jsDoc.isConstructor() || jsDoc.isInterface()) {
        // Always traverse constructors and interfaces
        return true;
      }
      if (jsDoc.hasThisType() || jsDoc.isOverride()) {
        // Do not traverse functions with @this or @override if they are not constructors or interfaces.
        return false;
      }
    }

    // Check parent node to decide traversal permissions.
    int pType = parent.getType();
    if (!(pType == Token.BLOCK ||
          pType == Token.SCRIPT ||
          pType == Token.NAME ||
          pType == Token.ASSIGN ||
          pType == Token.STRING ||
          pType == Token.NUMBER)) {
      return false;
    }
  }

  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();
    // Allow traversal on left-hand side of assignment
    if (n == lhs && lhs.getType() != Token.GETPROP) {
      return true;
    } else {
      // Do not traverse right-hand side if it assigns to a prototype's property
      if (NodeUtil.isGet(lhs)) {
        Node target = lhs.getFirstChild();
        String propName = lhs.getLastChild().getString();
        if (propName.equals("prototype")) {
          // Do not traverse assignments to prototypes.
          return false;
        }
      }
    }
  }

  return true;
}
