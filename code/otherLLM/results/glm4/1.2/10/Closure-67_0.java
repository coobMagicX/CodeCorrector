private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP) {

    // Check the first level of chaining
    boolean isFirstLevelChaining = n.getFirstChild().getType() == Token.GETPROP;
    if (!isFirstLevelChaining) {
      return false; // No chaining at first level, thus not a prototype property access
    }

    Node chainedNode = n.getFirstChild();
    Node secondLevelNode = chainedNode.getNext(); // This is the node after GETPROP

    // Check if the second level node is of type STRING and its value is "prototype"
    boolean isPrototypeAccess = (secondLevelNode.getType() == Token.STRING &&
                                 "prototype".equals(secondLevelNode.getString()));

    return isPrototypeAccess;
  }

  return false;
}