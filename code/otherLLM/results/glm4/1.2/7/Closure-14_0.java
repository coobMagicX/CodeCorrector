private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  
  // Case where we are at the end of script or returning from a function.
  if (node.getParent() == null || node.getParent().isFunction() ||
      (cfa != null && node == cfa.root)) {
    return new SymbolicReturnNode(); // Return a symbolic return node.
  }

  switch (node.getType()) {
    case Token.IF:
    case Token.WHILE:
    case Token.DO:
    case Token.FOR:
      // Control flows to the next sibling after these constructs.
      Node nextSibling = node.getNext();
      while (nextSibling != null && nextSibling.isFunction()) {
        nextSibling = nextSibling.getNext();
      }
      return nextSibling != null ? computeFallThrough(nextSibling) : node.getParent();

    case Token.CASE:
    case Token.DEFAULT_CASE:
      // Control flows to the body of the next case.
      if (node.getNext() != null && node.getNext().isCase()) {
        return node.getNext().getFirstChild();
      } else {
        return node.getParent(); // If no next case, go back up to parent.
      }

    case Token.TRY:
      // Handle TRY block.
      Node firstChild = node.getFirstChild();
      if (firstChild != null && firstChild.getType() == Token.CATCH) {
        // If a CATCH follows directly, control flows to its body or the next sibling.
        return computeFollowNode(node, firstChild.getNext(), cfa);
      } else if (NodeUtil.hasFinally(node)) {
        // If there is a FINALLY block, go to its node.
        Node finallyNode = NodeUtil.getFinallyNode(node);
        if (finallyNode != null) {
          return computeFallThrough(finallyNode);
        }
      }

      // Control flows to the next sibling after TRY or directly to the parent if no CATCH/FINALLY follows.
      return firstChild;

    case Token.CATCH:
      // Handle CATCH block by going to its body or the next sibling.
      return node.getNext() != null ? computeFollowNode(node, node.getNext(), cfa) : node.getParent();

    case Token.RETURN:
      // Directly return to the parent.
      return node.getParent();
  }

  // If none of the above cases apply, control flows to the next sibling.
  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }
  return nextSibling != null ? computeFallThrough(nextSibling) : node.getParent();
}

private static Node computeFallThrough(Node nextSibling) {
  // This method can be defined as needed to handle fall-through cases.
  return nextSibling;
}