private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }

  Preconditions.checkArgument(node.isGetProp());

  Node parentNode = node.getParent();
  Node grandparentNode = parentNode != null ? parentNode.getParent() : null;

  if (parentNode != null && parentNode.equals(replacement)) {
    return false;
  }

  if (grandparentNode != null && grandparentNode.equals(replacement)) {
    return false;
  }

  if (node.hasChildren()) {
    for (Node child : node.children()) {
      if (!isSafeReplacement(child, replacement)) {
        return false;
      }
    }
  }

  return true;
}