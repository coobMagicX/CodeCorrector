private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Check if child contains assigned name that would be replaced by new node
  Node child = node.getFirstChild();
  while (child != null) {
    if (child.isName() && isNameAssignedTo(child.getString(), replacement)) {
      return false;
    }
    child = child.getNextSibling();
  }

  return true;
}