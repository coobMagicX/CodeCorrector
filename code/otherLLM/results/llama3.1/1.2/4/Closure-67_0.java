private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && (n.getType() == Token.GETPROP || n.getType() == Token.SETPROP)
      ) {
    // We want to exclude the assignment itself from the usage list
    boolean isChainedProperty =
        n.getFirstChild().getType() == Token.GETPROP;

    if (isChainedProperty) {
      Node child = n.getFirstChild();
      while (child != null && child.getType() == Token.GETPROP) {
        child = child.getNext();
      }

      if (child != null && child.getType() == Token.STRING &&
          child.getString().equals("prototype")) {
        return true;
      }
    } else if (n.getType() == Token.SETPROP && n.getString().equals("prototype")) {
      return true;
    }
  }

  return false;
}