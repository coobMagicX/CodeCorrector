private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Check for nested assignments
  while (node != null && !node.isName()) {
    node = node.getFirstChild();
    if (node != null) {
      if (isNameAssignedTo(node.getString(), replacement)) {
        return false;
      }
    }
  }

  return true;
}