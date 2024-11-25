private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP
  ) {
    // We want to exclude the assignment itself from the usage list
    boolean isChainedProperty = false;
    while (n != null && n.getType() == Token.GETPROP) {
      if (n.getFirstChild().getType() == Token.STRING &&
          n.getFirstChild().getString().equals("prototype")) {
        return true; // Return immediately if "prototype" is found in the chain
      }
      n = n.getNextSibling(); // Move to the next sibling instead of trying to access a child
    }
  }

  return false;
}