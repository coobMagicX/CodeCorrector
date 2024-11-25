private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Recursively traverse the DOM tree to check for name assignment
  return !isNameAssignedToRecursively(node, replacement);

}

private boolean isNameAssignedToRecursively(Node node, Node replacement) {
  if (node == null) {
    return false;
  }
  
  if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
    return true;
  }

  // Recursively check child nodes
  return isNameAssignedToRecursively(node.getFirstChild(), replacement);
}