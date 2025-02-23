private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();

  if (parent == null) {
    return null;  // No parent means we're at the top level, ignore.
  }

  if (parent.isFunction()) {
    if (node.getType() != Token.RETURN) {
      // If the function expected a return value, we have to return null which symbolizes end of control
      if (cfa.functionReturnsValue(parent)) {
        return null;
      } else {
        // Otherwise, we return the parent, assuming control goes back to the caller.
        return parent;
      }
    } else {
      // Handle return by directly returning null, signaling end-of-flow from this path.
      return null;
    }
  }

  // Existing cases:
  switch (parent.getType()) {
    // Similar existing switch-case handling.
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.WHILE:
    case Token.DO:
      return parent;
    // Handle more cases as needed.
  }

  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    return computeFollowNode(fromNode, parent, cfa);
  }
}
