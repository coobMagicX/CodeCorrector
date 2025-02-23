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
          if (parent.getNext().isCase() || parent.getNext().isDefaultCase()) {
            return parent.getNext().getFirstChild();
          } else {
            Preconditions.checkState(false, "Not reachable");
          }
        } else {
          // If no more nodes after CASE/DEFAULT_CASE, delegate follow computation to the parent.
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
            return parent.getLastChild();
          } else {
            return computeFollowNode(fromNode, parent, cfa);
          }
        } else if (NodeUtil.getCatchBlock(parent) == node) {
          if (NodeUtil.hasFinally(parent)) {
            return parent.getLastChild();
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
