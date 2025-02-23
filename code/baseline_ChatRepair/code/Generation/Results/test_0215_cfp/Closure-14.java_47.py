// Improve the check around return paths to ensure we are covering functions with or without explicit return statements
protected static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || node.isReturn() || node.isThrow() ||
      (cfa != null && node == cfa.root) || (parent.isFunction() && !node.hasChildren())) {
    return computeSymbolicReturnNode(fromNode, parent, cfa);
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
          Preconditions.checkState(false, "Non-case/default following a case/default");
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

protected static Node computeSymbolicReturnNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  if (node.isFunction() || cfa == null) {
    return null;
  } else {
    // Code to handle symbolic return node creation
    return cfa.createSymbolicReturnNode();
  }
}
