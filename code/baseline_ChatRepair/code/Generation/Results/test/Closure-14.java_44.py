private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || node.getType() == Token.RETURN) {
    // In case of return or no parent, transfer control to symbolic return node, called null here for simplicity.
    return null;
  }
  
  if (parent.isFunction()) {
    // If the parent is a function, transfer control to the end of the function (symbolic return node).
    return null;
  }

  if (cfa != null && node == cfa.root) {
    // If the node is the root of the analysis, we should return null, as we've reached the end of the traversal.
    return null;
  }

  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null) {
        return computeFallThrough(parent.getNext().getFirstChild());
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
        return NodeUtil.hasFinally(parent) ? computeFallThrough(parent.getLastChild()) : computeFollowNode(fromNode, parent, cfa);
      } else if (NodeUtil.getCatchBlock(parent) == node) {
        return NodeUtil.hasFinally(parent) ? node.getNext() : computeFollowNode(fromNode, parent, cfa);
      } else if (parent.getLastChild() == node) {
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
