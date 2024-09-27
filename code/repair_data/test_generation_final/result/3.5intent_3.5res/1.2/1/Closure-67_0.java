private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign) && n.getType() == Token.GETPROP) {
    boolean isChainedProperty = n.getFirstChild().getType() == Token.GETPROP || n.getFirstChild().getNext().getType() == Token.GETPROP;

    if (isChainedProperty) {
      Node child = n.getFirstChild().getNext().getFirstChild().getNext();

      if (child.getType() == Token.STRING && child.getString().equals("prototype")) {
        return true;
      }
    }
  }

  return false;
}