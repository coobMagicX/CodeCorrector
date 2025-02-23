public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  // Check if current node is a function
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    
    // Skip traversing constructors or functions with @this or @override annotations
    if (jsDoc != null && (jsDoc.isConstructor() && shouldSkipTraversal(n, parent) ||
                          jsDoc.isInterface() ||
                          jsDoc.hasThisType() ||
                          jsDoc.isOverride())) {
      return false;
    }
  }

  // Logic for traversing left and right-hand side of an assignment
  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    Node rhs = lhs.getNext();
    boolean isTestingLeftHand = n == lhs;
    
    // Always traverse the left side of the assignment
    if (isTestingLeftHand && assignLhsChild == null) {
      assignLhsChild = lhs;
    } else if (!isTestingLeftHand) {
      // Only traverse the right side if not assigning to a prototype property
      if (isPrototypePropertyAssignment(lhs)) {
        return false;
      }
    }
  }

  return true;
}

private boolean shouldSkipTraversal(Node n, Node parent) {
  int pType = parent.getType();
  return !(pType == Token.BLOCK ||
           pType == Token.SCRIPT ||
           pType == Token.NAME ||
           pType == Token.ASSIGN ||
           pType == Token.STRING ||
           pType == Token.NUMBER);
}

private boolean isPrototypePropertyAssignment(Node lhs) {
  boolean directPrototypeAssignment = 
      lhs.getType() == Token.GETPROP && lhs.getLastChild().getString().equals("prototype");
  if (directPrototypeAssignment) return true;
  
  if (NodeUtil.isGet(lhs)) { // Check even deeper prototype assignment
    Node getTarget = lhs.getFirstChild();
    return getTarget.getType() == Token.GETPROP &&
           getTarget.getLastChild().getString().equals("prototype");
  }
  return false;
}
