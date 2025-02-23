private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  /*
   * Explanation remains untouched.
   */
  Node parent = node.getParent();
  
  if (parent == null || parent.isFunction() || node.isReturn() ||
      (cfa != null && node == cfa.root)) {
    // Including check if the current node is a RETURN, handling it correctly.
    return cfa.createSymbolicReturnNode(parent);
  }
  
  switch (parent.getType()) {
    // Existing case handlers with explanations untouched...
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    // Other cases ...
    case Token.FOR:
      if (NodeUtil.isForIn(parent)) {
        return parent;
      } else {
        return parent.getFirstChild().getNext().getNext();
      }
    case Token.WHILE:
    case Token.DO:
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
      break;
    // Additional cases...
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
