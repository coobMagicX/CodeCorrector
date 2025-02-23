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
      if (assignLhsChild == null) {
        assignLhsChild = lhs;
      }
    } else {
      // Correct the typo in the traversal checks to ensure it's not an assignment 
      // to a prototype property or subproperty, also considering the check for the string "prototype"
      if ((lhs.getType() == Token.GETPROP &&
            lhs.getLastChild().getString().equals("prototype")) ||
          (lhs.getQualifiedName() != null && lhs.getQualifiedName().contains(".prototype."))) {
          return false;
      }
    }
  }

  return true;
}
