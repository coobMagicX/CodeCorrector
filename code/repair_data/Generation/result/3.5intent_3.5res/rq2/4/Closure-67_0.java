private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP
      ) {
    // We want to exclude the assignment itself from the usage list
    boolean isChainedProperty =
        n.getFirstChild().getType() == Token.GETPROP;

    if (isChainedProperty) {
      Node child = n.getFirstChild().getFirstChild().getNext();

      if (child.getType() == Token.STRING &&
          (child.getString().equals("prototype") || isChainedPrototype(child))) {
        return true;
      }
    } else {
      Node child = n.getFirstChild().getNext();

      if (child.getType() == Token.STRING &&
          (child.getString().equals("prototype") || isChainedPrototype(child))) {
        return true;
      }
    }
  }

  return false;
}

private boolean isChainedPrototype(Node node) {
  Node next = node.getNext();
  if (next != null && next.getType() == Token.GETPROP) {
    Node grandChild = next.getFirstChild().getNext();
    if (grandChild.getType() == Token.STRING &&
        grandChild.getString().equals("prototype")) {
      return true;
    }
  }
  return false;
}