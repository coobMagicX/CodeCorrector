private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP
      ) {
    // We want to exclude the assignment itself from the usage list
    boolean isChainedProperty =
        n.getFirstChild().getType() == Token.GETPROP || n.getFirstChild().getType() == Token.STRING || n.getFirstChild().getType() == Token.GETELEM;

    if (isChainedProperty) {
      Node child = n.getFirstChild().getFirstChild().getNext();

      if ((child.getType() == Token.STRING && child.getString().equals("prototype")) ||
          (child.getType() == Token.GETELEM && child.getFirstChild().getString().equals("prototype"))) {
        return true;
      }
    }
  }

  return false;
}