private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP) {
    // We want to exclude the assignment itself from the usage list
    Node chainHead = n; // The potential head of a chained property access

    while (chainHead != null) {
      if (chainHead.getType() == Token.GETPROP) {
        chainHead = chainHead.getFirstChild();
      } else {
        break;
      }
    }

    if (chainHead != null && chainHead.getType() == Token.STRING &&
        chainHead.getString().equals("prototype")) {
      return true;
    }
  }

  return false;
}