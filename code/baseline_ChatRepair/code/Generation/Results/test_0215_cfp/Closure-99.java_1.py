public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    // Don't traverse functions that are constructors or have the @this or @override annotation.
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null && (jsDoc.isConstructor() || jsDoc.hasThisType() || jsDoc.isOverride())) {
      return false;
    }

    // Don't traverse functions unless they have potential to be given a @this annotation.
    int pType = parent.getType();
    if (!(pType == Token.BLOCK || pType == Token.SCRIPT || pType == Token.NAME || pType == Token.ASSIGN)) {
      return false;
    }
  }

  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    // rhs is not needed to be calculated as it's not used
    
    if (n != lhs) { // When current node is the right-hand side of assignment
      // Check if the assignment is to a prototype or subproperty
      while (lhs != null && lhs.getType() == Token.GETPROP) {
        if (lhs.getLastChild().getString().equals("prototype")) {
          // The assignment is directly to a prototype chain, thus must not traverse further
          return false;
        }
        lhs = lhs.getFirstChild();
      }
    }
  }

  return true;
}
