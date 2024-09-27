private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  node = node.getFirstChild();
  if (node.isName()
      && isNameAssignedTo(node.getString(), replacement)) {
    return false;
  }

  // Additional check for property chains
  Node parent = node;
  while (parent.isGetProp()) {
    parent = parent.getFirstChild();
  }
  if (parent.isName()
	&& isNameAssignedTo(parent.getString(), replacement)) {
	return false;
  }
  return true;
}