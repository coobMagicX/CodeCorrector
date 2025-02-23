private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
      (cfa != null && node == cfa.root)) {
    // Instead of `return null`, return a symbolic return node
    return cfa.createSymbolicReturnNode(node);
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
          throw new IllegalStateException("Not reachable");
        }
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
    case Token.FOR:
      return (NodeUtil.isForIn(parent)) ? parent : parent.getFirstChild().getNext().getNext();
    case Token.WHILE:
    case Token.DO:
      return parent;
    case Token.TRY:
      if (parent.getFirstChild() == node) {
        return (NodeUtil.hasFinally(parent)) ? 
               computeFallThrough(parent.getLastChild()) : 
               computeFollowNode(fromNode, parent, cfa);
      } else if (NodeUtil.getCatchBlock(parent) == node) {
        return (NodeUtil.hasFinally(parent)) ? 
               computeFallThrough(node.getNext()) : 
               computeFollowNode(fromNode, parent, cfa);
      } else if (parent.getLastChild() == node) {
        for (Node finallyNode : cfa.finallyMap.get(parent)) {
          cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
        }
        return computeFollowNode(fromNode, parent, cfa);
      }
      break;
  }

  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    if (nextSibling.getNext() == null) {
      // Create edge back to parent or create symbolic return if it's the last function 
      return computeFollowNode(fromNode, parent, cfa);
    }
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    return computeFollowNode(fromNode, parent, cfa);
  }
}
