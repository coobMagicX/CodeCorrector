private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP
      ) {
    // We want to exclude the assignment itself from the usage list
    boolean isChainedProperty =
        n.getFirstChild().getType() == Token.GETPROP;

    if (isChainedProperty) {
      Node child = n.getFirstChild().getFirstChild();

      if (child != null && child.getNext() != null &&
          child.getNext().getType() == Token.STRING &&
          child.getNext().getString().equals("prototype")) {
        return true;
      }
    }
  }

  return false;
}