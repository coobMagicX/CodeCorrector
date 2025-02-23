private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {

  Node parent = node.getParent();

  // Conditions for final control transfer or return.
  if (parent == null ||
      (cfa != null && node == cfa.root) ||
      // Return fetches to the caller when function body completes.
      node.isReturn() ||
      // Completing execution of function or script.
      parent.isFunction()) {

    // Assuming 'computeSymbolicReturnNode' returns an appropriate symbolic return node.
    return cfa == null ? null : cfa.computeSymbolicReturnNode(fromNode, node);
  }

  // Handle the logical block structures
  switch (parent.getType()) {
    case Token.IF:
    case Token.FOR:
    case Token.WHILE:
    case Token.DO:
    case Token.TRY:
      // For 'try', we must also handle in case there's a finalizer
      if (parent.getType() == Token.TRY && NodeUtil.hasFinally(parent) && node == NodeUtil.getCatchBlock(parent)) {
          return computeFallThrough(parent.getLastChild());
      }
    case Token.CASE:
    case Token.DEFAULT_CASE:
      return computeFollowNode(fromNode, parent, cfa);

    default:
      break;
  }

  // Traversing to the next executable node.
  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  // Computing fall through if no siblings are executable
  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    return computeFollowNode(fromNode, parent, cfa);
  }
}

private static Node computeFallThrough(Node node) {
  while (node != null && (node.isBlock() || node.isScript())) {
    node = node.getFirstChild();
  }
  return node;
}
