private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  node = node.getFirstChild();
  if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
    return false;
  }

  // Additional check to handle the scenario where a node is replaced by its parent node
  Node parent = node.getParent();
  if (parent != null && parent == replacement) {
    return false;
  }

  return true;
}

