private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  Node current = node.getFirstChild();
  while (current != null) {
    if (current.isName()
        && isNameAssignedTo(current.getString(), replacement)) {
      // Check for complex expressions or multiple assignments to the same property
      Node parent = current.getParent();
      while (parent != null) {
        if (parent.isAssign() && parent.getFirstChild() == current) {
          if (!isExpressionSafe(parent, replacement)) {
            return false;
          }
        }
        current = parent;
        parent = parent.getParent();
      }
    }
    // Move to the next sibling
    current = current.getNextSibling();
  }

  return true;
}

private boolean isExpressionSafe(Node node, Node replacement) {
  if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
    return false; // The expression contains an assignment that would not be safe after replacement.
  }
  // Further checks for complex expressions or property assignments could be added here
  // For now, we assume any other case is safe.
  return true;
}