private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP) {

    // Check for chained properties and skip the first GETPROP node.
    Node current = n;
    while (current.getType() == Token.GETPROP) {
      if (NodeUtil.isReference(current)) {
        return false; // Skip any references to avoid circular dependencies.
      }
      current = current.getFirstChild();
    }

    // Now check the last node after skipping chained properties.
    if (current.getType() == Token.STRING && current.getString().equals("prototype")) {
      return true;
    }
  }

  return false;
}