private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  Node currentNode = node.getFirstChild();
  while (currentNode != null) {
    if (currentNode.isName()
        && isNameAssignedTo(currentNode.getString(), replacement)) {
      return false;
    }
    if (!isSafeReplacement(currentNode, replacement)) {
      return false;
    }
    currentNode = currentNode.getNext();
  }

  return true;
}