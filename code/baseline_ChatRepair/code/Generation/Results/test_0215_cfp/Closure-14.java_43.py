private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  // Handling cases where a return to the calling context should occur.
  if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
    return null; // Transfer control to a symbolic return node or terminate.
  }

  // Handling nodes based on their types:
  switch (node.getType()) {
    // Direct return from inside a function should lead to the function's exit or call site.
    case Token.RETURN:
      if (parent.isFunction()) {
        Node pseudoReturnNode = new Node(Token.RETU, fromNode);
        // Create a pseudo return node or use a designated exit node if your analysis requires it
        if (cfa != null) {
          cfa.createEdge(fromNode, Branch.UNCOND, pseudoReturnNode);
        }
        return pseudoReturnNode;
      }
      break;
    case Token.BLOCK:
      Node lastChild = node.getLastChild();
      if (lastChild != null && lastChild.isReturn()) {
        return computeFollowNode(fromNode, lastChild, cfa);
      }
      break;
  }

  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    // Handle CASE and DEFAULT_CASE together:
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null && (parent.getNext().isCase() || parent.getNext().isDefaultCase())) {
        return parent.getNext().getFirstChild();
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
    // Handling loops and try-catch-finally structures similarly:
    case Token.FOR:
      if (!NodeUtil.isForIn(parent)) {
        return parent.getFirstChild().getNext().getNext();
      }
      break;
    case Token.WHILE:
    case Token.DO:
    case Token.TRY:
      return parent;
  }
  
  // Default handling for sequential siblings or moving up in the tree:
  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }
 
  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    // If no more siblings, control is transferred to parent follow node.
    return computeFollowNode(fromNode, parent, cfa);
  }
}
