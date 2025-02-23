private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction()) {
    // If there's a return statement, return that, else null.
    return cfa.expectedReturnType != null ? computeReturnNode(node) : null;
  }

  if (cfa != null && node == cfa.root) {
      return null;
  }

  // Handle other special cases
  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    // Additional cases ...
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
      // Extended TRY handling...
      // Additional cases for other parent types ...
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

private static Node computeReturnNode(Node node) {
    if (node.isReturn()) {
        return node;
    } else {
        Node childNode = node.getFirstChild();
        while (childNode != null) {
            Node returnNode = computeReturnNode(childNode);
            if (returnNode != null) {
                return returnNode;
            }
            childNode = childNode.getNext();
        }
    }
    return null;
}
