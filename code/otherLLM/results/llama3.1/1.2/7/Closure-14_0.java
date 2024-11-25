private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
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
          Node nextSibling = parent.getNext().getFirstChild().getNext();
          while (nextSibling != null && nextSibling.isFunction()) {
            nextSibling = nextSibling.getNext();
          }
          return computeFallThrough(nextSibling);
        } else if (parent.getNext().isDefaultCase()) {
          Node nextSibling = parent.getNext().getFirstChild();
          while (nextSibling != null && nextSibling.isFunction()) {
            nextSibling = nextSibling.getNext();
          }
          return computeFallThrough(nextSibling);
        } else {
          Preconditions.checkState(false, "Not reachable");
        }
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
      break;
    case Token.FOR:
      if (NodeUtil.isForIn(parent)) {
        return parent;
      } else {
        Node nextSibling = parent.getFirstChild().getNext().getNext();
        while (nextSibling != null && nextSibling.isFunction()) {
          nextSibling = nextSibling.getNext();
        }
        return computeFallThrough(nextSibling);
      }
    case Token.WHILE:
    case Token.DO:
      return parent;
    case Token.TRY:
      if (parent.getFirstChild() == node) {
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(parent.getLastChild());
        } else { // and have no FINALLY.
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (NodeUtil.getCatchBlock(parent) == node){
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          Node finallyMap = cfa.finallyMap.get(parent).iterator().next();
          cfa.createEdge(fromNode, Branch.UNCOND, finallyMap);
          return computeFollowNode(fromNode, parent, cfa);
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (parent.getLastChild() == node){
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
    // If there are no more siblings, control is transferred up the AST.
    return computeFollowNode(fromNode, parent, cfa);
  }
}