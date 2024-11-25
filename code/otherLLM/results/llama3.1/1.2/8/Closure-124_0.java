private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  // Check the first child of the node to see if it's a name that's assigned to
  Node child = node.getFirstChild();
  while (child != null) {
    if (child.isName() && isNameAssignedTo(child.getString(), replacement)) {
      return false;
    }
    child = child.getNextSibling();
  }

  return true;
}