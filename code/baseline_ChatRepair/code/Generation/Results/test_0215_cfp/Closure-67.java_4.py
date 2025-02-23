private boolean isPrototypePropertyAssign(Node assign) {
  if (assign == null || assign.getFirstChild() == null) {
    return false;
  }

  Node n = assign.getFirstChild();

  // Traverse the properties to find a "prototype" assignment
  while (n.getType() == Token.GETPROP) {
    Node property = n.getSecondChild();  // Get the property node
    if (property != null && property.getString().equals("prototype")) {
      return true;
    }
    n = n.getFirstChild();  // Move to the left side of the GETPROP
  }

  return false;
}
