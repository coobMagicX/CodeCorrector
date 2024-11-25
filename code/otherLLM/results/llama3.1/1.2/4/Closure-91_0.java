public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

  if (n.getType() == Token.FUNCTION) {
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

    // Don't traverse functions that are getting lent to a prototype.
    if (parent != null && parent.getType() == Token.ASSIGN) {
      Node lhs = parent.getFirstChild();
      Node rhs = lhs.getNext();

      if (n == lhs) {
        // Always traverse the left side of the assignment. To handle
        // nested assignments properly (e.g., (a = this).property = c;),
        // assignLhsChild should not be overridden.
        if (assignLhsChild == null) {
          assignLhsChild = lhs;
        }
      } else {
        // Only traverse the right side if it's not an assignment to a prototype
        // property or subproperty.
        if (NodeUtil.isGet(lhs)) {
          if (lhs.getType() == Token.GETPROP &&
              lhs.getLastChild().getString().equals("prototype")) {
            return false;
          }
          Node llhs = lhs.getFirstChild();
          if (llhs.getType() == Token.GETPROP &&
              llhs.getLastChild().getString().equals("prototype")) {
            return false;
          }
        } else {
          // Check for function being lent to a prototype
          if (rhs.getType() == Token.FUNCTION) {
            JSDocInfo jsDoc = getFunctionJsDocInfo(rhs);
            if (jsDoc != null && jsDoc.isLends()) {
              return false;
            }
          }
        }
      }
    }

  }

  return true;
}