private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Start from the first child and check for potential conflicts
  Node current = node.getFirstChild();
  while (current != null) {
    if (current.isName() && isNameAssignedTo(current.getString(), replacement)) {
      return false; // Conflict found, not a safe replacement
    }
    current = current.getNext(); // Move to the next sibling
  }

  return true; // No conflicts found, it's a safe replacement
}