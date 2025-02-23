public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() || jsDoc.isInterface())) {

      // For constructors, always traverse unless explicitly forbidden by @this or @override
      if (jsDoc.hasThisType() || jsDoc.isOverride()) {
        return false;
      }
    } else if (jsDoc != null && (jsDoc.hasThisType() || jsDoc.isOverride())) {
      // For non-constructors with @this or @override annotations, do not traverse
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
      // Check for assignments to properties, especially avoiding prototype pollution.
      // But do not skip traversal if it's a direct prototype assignment in global scope.
      if (NodeUtil.isGet(lhs) && lhs.getFirstChild().getType() != Token.THIS) {
        if (lhs.getLastChild().getString().equals("prototype")) {
          return false;
        }
        Node llhs = lhs.getFirstChild();
        if (llhs.getType() == Token.GETPROP && llhs.getLastChild().getString().equals("prototype")) {
          return false;
        }
      }
    }
  }

  return true;
}
