private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  // Handle the case where the node is a return statement, differently
  if (node.getType() == Token.RETURN) {
    // If the node is a return statement, we might want to return null or handle the return specifically
    if (parent != null && parent.isFunction()) {
      return null; // Returning from a function should signify end of control flow within that function scope
    }
  }

  if (parent == null || (cfa != null && node == cfa.root)) {
    return null;
  }

  if (parent.isFunction()) {
    // This block should only be reached if node is not a RETURN, comply with the function returns
    return null;
  }

  // If we are just before a IF/WHILE/DO/FOR:
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
          Preconditions.checkState(false, "Not reachable");
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
      } else if (NodeUtil.getCatchBlock(parent) == node){
        if (NodeUtil.hasFinally(parent)) {
          return computeFallThrough(node.getNext());
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
    return computeFollowNode(fromNode, parent, cfa);
  }
}
