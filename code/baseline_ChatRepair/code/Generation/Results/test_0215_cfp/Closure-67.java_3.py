private boolean isPrototypePropertyAssign(Node assign) {
  if (assign == null || assign.getFirstChild() == null) {
    return false;
  }

  Node n = assign.getFirstChild();

  while (n != null && n.isGetProp()) {
    Node propNode = n.getSecondChild();
    if (propNode != null && propNode.isString() && propNode.getString().equals("prototype")) {
      return true;
    }
    n = n.getFirstChild();
  }

  return false;
}
