protected Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction()) {
    if (parent != null && parent.isFunction() && node != parent.getLastChild()) {
      // Ensure all paths in non-void functions have a return
      if (!NodeUtil.isFunctionVoid(parent)) {
        return NodeUtil.createSymReturnNode(); // createSymReturnNode() should be appropriately defined
      }
    }
    return null;
  }

  if (cfa != null && node == cfa.root) {
    return null;
  }

  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null) {
        return computeFirstCaseChild(parent.getNext());
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
    case Token.FOR:
      return NodeUtil.isForIn(parent) ? parent : parent.getFirstChild().getNext().getNext();
    case Token.WHILE:
    case Token.DO:
      return parent;
    case Token.TRY:
      return computeFollowNodeInTry(fromNode, parent, node, cfa);
  }

  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  return nextSibling != null ? computeFallThrough(nextSibling) : computeFollowNode(fromNode, parent, cfa);
}

private Node computeFollowNodeInTry(Node fromNode, Node parent, Node node, ControlFlowAnalysis cfa) {
  Node firstChild = parent.getFirstChild();
  if (firstChild == node) {
    return NodeUtil.hasFinally(parent) ? computeFallThrough(parent.getLastChild()) : computeFollowNode(fromNode, parent, cfa);
  } else if (NodeUtil.getCatchBlock(parent) == node) {
    return NodeUtil.hasFinally(parent) ? computeFallThrough(node.getNext()) : computeFollowNode(fromNode, parent, cfa);
  } else if (parent.getLastChild() == node) {
    if (cfa.finallyMap.containsKey(parent)) {
      for (Node finallyNode : cfa.finallyMap.get(parent)) {
        cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
      }
    }
    return computeFollowNode(fromNode, parent, cfa);
  }
  return null;
}

private Node computeFirstCaseChild(Node caseNode) {
  return caseNode.isCase() ? caseNode.getFirstChild().getNext() : caseNode.getFirstChild();
}

private Node computeFallThrough(Node node) {
  // Define or utilize an existing method to handle computing the next node
  return node; // Placeholder, modify as needed
}
