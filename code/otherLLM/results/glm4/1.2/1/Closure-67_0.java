private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP) {

    // Check for chained GETPROP until reaching a non-GETPROP node or the end
    while (n != null && n.getType() == Token.GETPROP) {
      n = n.getFirstChild();
    }

    // If we have reached a STRING token and it's "prototype", return true
    if (n != null && n.getType() == Token.STRING &&
        n.getString().equals("prototype")) {
      return true;
    }
  }

  return false;
}