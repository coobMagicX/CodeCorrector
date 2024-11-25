private boolean isSafeReplacement(Node node, Node replacement) {
  // Input validation for the replacement parameter to prevent potential null pointer exceptions.
  Preconditions.checkArgument(replacement != null);

  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  
  // Edge case handling: Check if the input nodes are valid
  if (node == null || replacement == null) {
    return false; // or throw an exception, depending on your requirements
  }

  Preconditions.checkArgument(node.isGetProp());

  node = node.getFirstChild();
  if (node.isName()
      && isNameAssignedTo(node.getString(), replacement)) {
    return false;
  }

  return true;
}