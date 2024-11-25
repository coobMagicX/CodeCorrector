private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  while (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP) {
    
    // Check if the node is part of a property chain that leads to 'prototype'
    if (n.getFirstChild().getType() == Token.GETPROP) {
      Node grandchild = n.getFirstChild().getFirstChild();
      while (grandchild != null && grandchild.getType() == Token.GETPROP) {
        // Move to the next GETPROP node in the chain
        grandchild = grandchild.getNext();

        if (grandchild != null && grandchild.getType() == Token.STRING &&
            grandchild.getString().equals("prototype")) {
          return true; // Directly found 'prototype', return true
        }
      }
    }

    // Move to the next node in the original chain
    n = n.getNext();
  }

  return false;
}