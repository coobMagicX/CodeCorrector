private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  Node childNode = node.getFirstChild();
  while (childNode != null && !childNode.isLeaf()) {
    childNode = childNode.getNextSibling();
  }
  
  if (childNode != null 
      && childNode.isName()
      && isNameAssignedTo(childNode.getString(), replacement)) {
    return false;
  }

  return true;
}