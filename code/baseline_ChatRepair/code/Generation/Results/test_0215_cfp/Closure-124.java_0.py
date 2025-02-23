private boolean isSafeReplacement(Node node, Node replacement) {
  // Start with the deepest node and work upwards.
  while (!node.isName()) {
    if (node.isGetProp()) {
      node = node.getFirstChild();
    } else {
      // This guarantees that we only process names or proper property accesses.
      return true; 
    }
  }

  // If we get here, node should be a Name node.
  if (isNameAssignedTo(node.getString(), replacement)) {
    return false;
  }

  return true;
}
