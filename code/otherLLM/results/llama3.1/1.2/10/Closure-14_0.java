private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  // ... (rest of the code remains the same)

  switch (parent.getType()) {
    // The follow() of any of the path from IF would be what follows IF.
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      // After the body of a CASE, the control goes to the body of the next
      // case, without having to go to the case condition.
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
      break;
    case Token.FOR:
      if (NodeUtil.isForIn(parent)) {
        return parent;
      } else {
        // Check if the next node is a function call
        Node nextSibling = parent.getFirstChild().getNext();
        if (nextSibling != null && nextSibling.isFunctionCall()) {
          return computeFollowNode(fromNode, nextSibling, cfa);
        }
        return parent.getFirstChild().getNext().getNext();
      }
    case Token.WHILE:
    case Token.DO:
      // Check if the next node is a function call
      Node nextSibling = parent.getNext();
      if (nextSibling != null && nextSibling.isFunctionCall()) {
        return computeFollowNode(fromNode, nextSibling, cfa);
      }
      return parent;
    case Token.TRY:
      // If we are coming out of the TRY block...
      if (parent.getFirstChild() == node) {
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(parent.getLastChild());
        } else { // and have no FINALLY.
          return computeFollowNode(fromNode, parent, cfa);
        }
      // CATCH block.
      } else if (NodeUtil.getCatchBlock(parent) == node){
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(node.getNext());
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      // If we are coming out of the FINALLY block...
      } else if (parent.getLastChild() == node){
        if (cfa != null) {
          for (Node finallyNode : cfa.finallyMap.get(parent)) {
            cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
          }
        }
        return computeFollowNode(fromNode, parent, cfa);
      }
  }

  // Now that we are done with the special cases follow should be its
  // immediate next sibling. If it's a function call, we need to recursively 
  // compute the follow of the function call.
  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunctionCall()) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFollowNode(fromNode, nextSibling, cfa);
  } else {
    // If there are no more siblings, control is transferred up the AST.
    return computeFollowNode(fromNode, parent, cfa);
  }
}