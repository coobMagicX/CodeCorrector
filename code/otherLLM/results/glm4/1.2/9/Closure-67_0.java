private boolean isPrototypePropertyAssign(Node assign) {
  Node lhs = assign.getFirstChild();
  if (lhs != null && NodeUtil.isVarOrSimpleAssignLhs(lhs, assign)
      && lhs.getType() == Token.GETPROP) {
    
    // Check if the property assignment is chained
    Node chainNode = lhs.getFirstChild();
    while (chainNode != null && chainNode.getType() == Token.GETPROP) {
      chainNode = chainNode.getNextSibling(); // Move to the next node in the chain
    }
    
    // Now check if the end of the chain is a STRING with value "prototype"
    if (chainNode != null && chainNode.getType() == Token.STRING &&
        chainNode.getString().equals("prototype")) {
      return true;
    }
  }

  return false;
}