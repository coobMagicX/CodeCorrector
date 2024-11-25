private boolean isSafeReplacement(Node node, Node replacement) {
  // Validate `replacement` node
  Preconditions.checkNotNull(replacement);
  
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Handle complex names
  while (true) {
    Node child = node.getFirstChild();
    if (child == null || !child.isName()) break;

    node = child;
  }

  // Check for recursive replacements
  if (node.equals(replacement)) return false;

  if (node.isName()
      && isNameAssignedTo(node.getString(), replacement)) {
    return false;
  }

  return true;
}