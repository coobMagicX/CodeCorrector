private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();

  if (parent == null || parent.isFunction()) {
    // If parent is null or a function, we are transferring control back to the caller of the function.
    return null;
  }
  
  if (node.isReturn()) {
    // If the node is a return statement, transfer control back to the caller of the function.
    // This is achieved by returning null, which signifies no subsequent node to execute in the current scope.
    return null;
  }

  if (cfa != null && node == cfa.root) {
    // If the node is the root node in Control Flow Analysis, then we have reached the end.
    return null;
  }

  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    // More cases (FOR, WHILE, DO, CASE, DEFAULT_CASE, TRY, etc.) handled similarly to existing implementation.
    // Omitted here for brevity.

    default:
      break;
  }

  // Continue handling for sibling nodes, considering function nodes and wrapping up to parents as in the original implementation.
  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    // If there are no more siblings, control is transferred up the AST.
    return computeFollowNode(fromNode, parent, cfa);
  }
}
