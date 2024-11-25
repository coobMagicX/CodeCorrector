private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {

  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
      (cfa != null && node == cfa.root)) {
    return null;
  }

  // If we are just before a IF/WHILE/DO/FOR:
  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null) {
        if (parent.getNext().isCase()) {
          return parent.getNext().getFirstChild().getNext();
        } else if (parent.getNext().isDefaultCase()) {
          return parent.getNext().getFirstChild();
        }
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
      break;
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
      // If we are coming out of the TRY block...
      if (parent.getFirstChild() == node) {
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(parent.getLastChild());
        } else { // and have no FINALLY.
          return computeFollowNode(fromNode, parent.getNext(), cfa); // Fix: Follow the next sibling instead of parent
        }
      // CATCH block.
      } else if (NodeUtil.getCatchBlock(parent) == node){
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(node.getNext());
        } else {
          return computeFollowNode(fromNode, parent.getNext(), cfa); // Fix: Follow the next sibling instead of parent
        }
      // If we are coming out of the FINALLY block...
      } else if (parent.getLastChild() == node){
        if (cfa != null) {
          for (Node finallyNode : cfa.finallyMap.get(parent)) {
            cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
          }
        }
        return computeFollowNode(fromNode, parent, cfa); // Fix: Continue to follow the parent
      }
  }

  // Now that we are done with the special cases follow should be its
  // immediate sibling, unless its sibling is a function
  Node nextSibling = node.getNext();

  // Skip function declarations because control doesn't get pass into it.
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

private static Node computeFallThrough(Node node) {
  // Implementation of computeFallThrough method
}