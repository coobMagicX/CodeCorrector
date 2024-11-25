private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
      (cfa != null && node == cfa.root)) {
    return null;
  }

  switch (parent.getType()) {
    case Token.IF:
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (node == parent.getLastChild()) { // Check if we are at the end of a CASE block
        Node nextNode = parent.getNext();
        if (nextNode != null && nextNode.isCase()) {
          return nextNode.getFirstChild().getNext();
        } else if (nextNode != null && nextNode.isDefaultCase()) {
          return nextNode.getFirstChild();
        }
      }
      return computeFollowNode(fromNode, parent, cfa);
    case Token.FOR:
      if (NodeUtil.isForIn(parent)) {
        return parent;
      } else {
        return parent.getFirstChild().getNext().getNext();
      }
    case Token.WHILE:
    case Token.DO:
      return parent;
    case Token.TRY:
      if (parent.getFirstChild() == node) { // Inside TRY block
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(parent.getLastChild());
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (NodeUtil.getCatchBlock(parent) == node) { // Inside CATCH block
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(node.getNext());
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (parent.getLastChild() == node) { // Exiting FINALLY block
        Node catchNode = getExceptionHandler(node);
        if (catchNode != null) {
          cfa.createEdge(fromNode, Branch.UNCOND, catchNode);
        }
        return computeFollowNode(fromNode, parent, cfa);
      }
  }

  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) { // Skip function declarations
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    return computeFollowNode(fromNode, parent, cfa); // Transfer control up the AST
  }
}