private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  
  Preconditions.checkArgument(node.isGetProp());
  
  // Check complex name assignments in parent-child relationships
  Node parentNode = node.getParentNode();
  while (parentNode != null && !parentNode.isName()) {
    parentNode = parentNode.getParentNode();
  }
  
  if (parentNode != null) {
    String parentString = parentNode.getString();
    if (!parentString.isEmpty() && isNameAssignedTo(parentString, replacement)) {
      return false;
    }
  }

  // Check current node's name assignments
  if (node.isName()
      && isNameAssignedTo(node.getString(), replacement)) {
    return false;
  }

  return true;
}