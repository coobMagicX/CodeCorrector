private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && (n.getType() == Token.GETPROP || n.getType() == Token.GETELEM)) {
    // We want to exclude the assignment itself from the usage list
    boolean isChainedProperty =
        n.getFirstChild().getType() == Token.GETPROP;

    if (isChainedProperty) {
      Node child = n.getFirstChild().getFirstChild().getNext();

      if (child.getType() == Token.STRING &&
          child.getString().equals("prototype")) {
        return true;
      }
    } else if (n.getType() == Token.GETELEM) {
      Node child = n.getLastChild();

      if (child.getType() == Token.STRING &&
          child.getString().equals("prototype")) {
        return true;
      }
    }

    // Check if the parent of the assignment is an expression statement
    Node parent = assign.getParent();
    if (parent != null && parent.isExprResult()) {
      Node grandparent = parent.getParent();
      if (grandparent != null && grandparent.isCall()) {
        Node functionName = grandparent.getFirstChild();
        if (functionName.getType() == Token.GETPROP) {
          Node object = functionName.getFirstChild();
          if (object.getType() == Token.NAME && object.getString().equals("Object")) {
            Node method = functionName.getLastChild();
            if (method.getType() == Token.STRING &&
                (method.getString().equals("assign") || method.getString().equals("defineProperties"))) {
              return true;
            }
          }
        }
      }
    }
  }

  return false;
}