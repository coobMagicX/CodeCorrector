private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
    if (!node.isFunction()) {
      Node endNode = new Node(Token.RETURN);  // Simulate a symbolic return node.
      endNode.setLineno(node.getLineno());
      return endNode;
    }
    return null;
  }

  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null) {
        return parent.getNext().getFirstChild();
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
      } else if (NodeUtil.getCatchBlock(parent) == node){
        return NodeUtil.hasFinally(parent) ? computeFallThrough(node.getNext()) : computeFollowNode(fromNode, parent, cfa);
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

  return nextSibling != null ? computeFallThrough(nextSibling) : computeFollowNode(fromNode, parent, cfa);
}
