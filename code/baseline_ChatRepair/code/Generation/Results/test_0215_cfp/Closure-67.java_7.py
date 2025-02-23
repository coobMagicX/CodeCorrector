private boolean isPrototypePropertyAssign(Node assign) {
  Node n = assign.getFirstChild();
  if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
      && n.getType() == Token.GETPROP) {
    // Check if the right-most property being accessed is "prototype"
    Node property = n.getLastChild();
    if (property != null && property.getType() == Token.STRING
        && "prototype".equals(property.getString())) {
      return true;
    }
  }
  return false;
}
