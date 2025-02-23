private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP) {
    // Drill down to the most nested GETPROP to check if it's "prototype"
    while (n.getFirstChild() != null && n.getFirstChild().getType() == Token.GETPROP) {
      n = n.getFirstChild();
    }
    
    // Now, n should be at level just above "prototype". Check now.
    Node target = n.getFirstChild();
    Node property = n.getLastChild();
    if (target != null && property != null 
        && property.getType() == Token.STRING 
        && "prototype".equals(property.getString())) {
      return true;
    }
  }
  return false;
}
