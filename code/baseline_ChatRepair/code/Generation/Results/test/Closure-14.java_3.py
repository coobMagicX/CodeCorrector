private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();

  // Handling the case when node is a return statement
  if (node.isReturn()) {
    if (parent != null && parent.isFunction()) {
      return null; // A return in a function means control should go back to the caller of the function
    }
    // Otherwise, normal control flow to the next node
    // fall-through can include more cases for RETURN inside other control structures if needed
  }

  if (parent == null || (parent.isFunction() && !node.isReturn()) ||
      (cfa != null && node == cfa.root)) {
    return null;
  }

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
        } else {
          Preconditions.checkState(false, "Not reachable");
        }
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
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
      if (parent.getFirstChild() == node) {
        if (NodeUtil.hasFinally(parent)) {
          return computeFallThrough(parent.getLastChild());
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (NodeUtil.getCatchBlock(parent) == node) {
        if (NodeUtil.hasFinally(parent)) {
          return computeFallThrough(node.getNext());
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (parent.getLastChild() == node) {
        if (cfa != null) {
          for (Node finallyNode : cfa.finallyMap.get(parent)) {
            cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
          }
        }
        return computeFollowNode(fromNode, parent, cfa);
      }
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
