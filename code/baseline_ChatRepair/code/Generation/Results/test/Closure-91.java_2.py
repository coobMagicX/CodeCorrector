public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null) {
      // Check for constructor, interface or override, these should still be traversed 
      // but handle the exception for constructors with global this context
      if (jsDoc.isConstructor() && n.getFirstChild().getType() != Token.THIS) {
        return true;  // Always traverse constructors unless they incorrectly reference global this
      } else if (jsDoc.isInterface() || jsDoc.hasThisType() || jsDoc.isOverride()) {
        return false;  // Do not traverse functions if they have this type or are overriding, except for constructors.
      }
    }
  }
  
  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();

    // Avoid traversing assignments that are directly to a prototype property
    if (NodeUtil.isLValue(lhs) && lhs.getType() == Token.GETPROP) {
      Node propNode = lhs.getSecondChild();
      if (propNode.getString().equals("prototype")) {
        // Avoid traversing the right side of assignments to `prototype` properties
        if (n == lhs.getNext()) {
          return false;
        }
      }
    }
  }
  
  // Default to allowing traversal
  return true;
}
