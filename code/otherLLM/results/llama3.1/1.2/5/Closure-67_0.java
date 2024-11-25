private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP
      ) {
    // We want to exclude the assignment itself from the usage list
    boolean isChainedProperty =
        NodeUtil.isGetPropertyChain(n);

    if (isChainedProperty) {
      Node child = getNestedChildNode(n);
      
      if (child != null && 
          child.getType() == Token.STRING &&
          child.getString().equals("prototype")) {
        return true;
      }
    }
  }

  return false;
}

private Node getNestedChildNode(Node n) {
  Node child = n.getFirstChild();
  while (child != null && child.getType() != Token.GETPROP) {
    child = child.getNext();
  }
  
  if (child == null || child.getType() != Token.GETPROP) {
    return null;
  }

  child = child.getFirstChild();
  while (child != null && child.getType() != Token.STRING) {
    child = child.getNext();
  }
  
  return child;
}