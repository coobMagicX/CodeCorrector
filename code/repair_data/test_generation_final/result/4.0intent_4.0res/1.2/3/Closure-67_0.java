private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)) {
    boolean isPrototypeAssignment = false;

    if (n.getType() == Token.GETPROP) {
      isPrototypeAssignment = checkPrototypeInPropertyChain(n);
    } else if (n.getType() == Token.GETELEM) {
      isPrototypeAssignment = checkPrototypeInElementChain(n);
    }

    return isPrototypeAssignment;
  }

  return false;
}

private boolean checkPrototypeInPropertyChain(Node n) {
  Node firstChild = n.getFirstChild();
  if (firstChild.getType() == Token.GETPROP) {
    Node child = firstChild.getFirstChild().getNext();
    return child.getType() == Token.STRING && "prototype".equals(child.getString());
  }
  return false;
}

private boolean checkPrototypeInElementChain(Node n) {
  Node firstChild = n.getFirstChild();
  if (firstChild.getType() == Token.GETELEM) {
    Node child = firstChild.getFirstChild().getNext();
    return child.getType() == Token.STRING && "prototype".equals(child.getString());
  }
  return false;
}