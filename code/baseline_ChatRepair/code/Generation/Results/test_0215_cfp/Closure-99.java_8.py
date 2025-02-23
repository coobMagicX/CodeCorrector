public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null && (jsDoc.isConstructor() || jsDoc.hasThisType() || jsDoc.isOverride())) {
      // Skip traversing functions annotated as constructors, with @this, or @override
      return false;
    }

    int pType = parent.getType();
    if (!(pType == Token.BLOCK || pType == Token.SCRIPT || pType == Token.NAME || pType == Token.ASSIGN)) {
      // Limit function traverse to specific parent node types
      return false;
    }
  }

  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();
    
    if (lhs.getType() == Token.GETPROP &&
        (lhs.getLastChild().getString().equals("prototype") || lhs.getQualifiedName().contains(".prototype."))) {
      // Disable traversal on the right side if it's an assignment to prototype property
      return false;
    }
  }

  return true;
}
