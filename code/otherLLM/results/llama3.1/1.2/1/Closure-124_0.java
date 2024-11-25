private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  Node currentNode = node;
  do {
    if (currentNode.isAssign() && currentNode.getString().equals(replacement.getString())) {
      return false; // If the replacement is an assign with the same name, it's not safe
    }
    currentNode = currentNode.getFirstChild();
  } while (currentNode != null);

  return true;
}