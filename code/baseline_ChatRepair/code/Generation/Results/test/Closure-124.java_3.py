private boolean isSafeReplacement(Node node, Node replacement) {
  // Initial check to prevent replacing important nodes.
  if (node == null || replacement == null) {
    return false;
  }
  
  // No checks are needed for simple names, but ensure both node and replacement are indeed simple names.
  if (node.isName() && replacement.isName()) {
    return true;
  }
  
  // Ensure the node is a property and handle nested names safely.
  if (node.isGetProp()) {
    Node currentNode = node.getFirstChild();
    while (currentNode.isGetProp()) {
      currentNode = currentNode.getFirstChild();
    }

    if (currentNode.isName() && isNameAssignedTo(currentNode.getString(), replacement)) {
      return false;
    }
  }

  return true;
}
