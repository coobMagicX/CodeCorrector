private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  Node parentNode = node.getParent();
  if (parentNode != null && (parentNode == replacement || parentNode.getParent() == replacement)) {
    return false;
  }

  node = node.getFirstChild();
  if (node.isGetProp()
      && isNameAssignedTo(node.getFirstChild().getString(), replacement)) {
    return false;
  }

  return true;
}