private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Store the first child in a temporary variable to check after reassignment.
  Node firstChild = node.getFirstChild();
  
  if (firstChild != null && firstChild.isName()
      && isNameAssignedTo(firstChild.getString(), replacement)) {
    return false;
  }

  return true;
}