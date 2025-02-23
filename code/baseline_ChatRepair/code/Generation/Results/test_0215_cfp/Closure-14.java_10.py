private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction()) {
    return null;
  }

  // Handle the return statement explicitly.
  if (node.isReturn()) {
    if (parent.isFunction()) {
      return null; // return in a function need not follow to another sibling.
    } else {
      // Outside a function, find appropriate follow node.
      return computeFollowNode(fromNode, parent, cfa);
    }
  }

  if (cfa != null && node == cfa.root) {
    return null; // Handle the root node.
  }

  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      // Handling for CASE and DEFAULT_CASE
      if (parent.getNext() != null) {
        if (parent.getNext().isCase() || parent.getNext().isDefaultCase()) {
          return parent.getNext().getFirstChild();
        } else {
          Preconditions.checkState(false, "Not reachable");
        }
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
    case Token.FOR:
      return NodeUtil.isForIn(parent) ? parent : parent.getFirstChild().getNext().getNext();
    case Token.WHILE:
    case Token.DO:
      return parent;
    case Token.TRY:
      if (parent.getFirstChild() == node) {
        return NodeUtil.hasFinally(parent) ? computeFallThrough(parent.getLastChild()) : computeFollowNode(fromNode, parent, cfa);
      } else if (NodeUtil.getCatchBlock(parent) == node) {
        return NodeUtil.hasFinally(parent) ? computeFallThrough(node.getNext()) : computeFollowNode(fromNode, parent, cfa);
      } else if (parent.getLastChild() == node) {
        handleFinallyInTry(cfa, fromNode, parent);
        return computeFollowNode(fromNode, parent, cfa);
      }
      break;
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

private static void handleFinallyInTry(ControlFlowAnalysis cfa, Node fromNode, Node tryNode) {
  if (cfa != null) {
    for (Node finallyNode : cfa.finallyMap.get(tryNode)) {
      cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
    }
  }
}
