private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Check if the replacement is a parent or grandparent of the current node
  Node parent = node.getParent();
  while (parent != null) {
    if (parent.equals(replacement)) {
      return false;
    }
    parent = parent.getParent();
  }

  node = node.getFirstChild();
  if (node.isName()
      && isNameAssignedTo(node.getString(), replacement)) {
    return false;
  }

  return true;
}