public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  // Don't traverse functions that are constructors or have the @this
  // or @override annotation.
  JSDocInfo jsDoc = getFunctionJsDocInfo(n);
  if (jsDoc != null &&
      (jsDoc.isConstructor() ||
       jsDoc.isInterface() ||
       jsDoc.hasThisType() ||
       jsDoc.isOverride())) {
    return false;
  }

  // Don't traverse functions unless they would normally
  // be able to have a @this annotation associated with them. e.g.,
  // var a = function() { }; // or
  // function a() {} // or
  // a.x = function() {}; // or
  // var a = {x: function() {}};
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

  // Check if the function is being lent to a prototype.
  if (n.getType() == Token.FUNCTION) {
    Node lhs = null;
    Node rhs = null;

    if (parent != null && parent.getType() == Token.ASSIGN) {
      lhs = parent.getFirstChild();
      rhs = lhs.getNext();

      // Check if the function is being assigned to a prototype property.
      if (lhs == n) { // If n is on the left side of an assignment
        Node current = n;
        while (current != null && NodeUtil.isGet(current)) {
          current = current.getFirstChild();
        }
        if (NodeUtil.isGetProp(current) && "prototype".equals(current.getLastChild().getString())) {
          return false; // Do not traverse this function as it is lent to a prototype.
        }
      }
    }

    // If the function is not being lent to a prototype, allow traversal.
  }

  return true;
}