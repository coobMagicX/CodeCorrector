private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  // ...
  
  case Token.TRY:
    if (parent.getFirstChild() == node) { // If we are coming out of the TRY block...
      if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
        return computeFallThrough(parent.getLastChild()); // Should be parent.getLastChild().getNext();
      } else { // and have no FINALLY.
        Node nextSibling = node.getNext();
        if (nextSibling != null && !nextSibling.isFunction()) {
          return computeFallThrough(nextSibling);
        }
        return computeFollowNode(fromNode, node.getParent(), cfa); // Otherwise fall back to parent
      }
    } else if (NodeUtil.getCatchBlock(parent) == node) { // CATCH block.
      Node nextSibling = node.getNext();
      if (nextSibling != null && !nextSibling.isFunction()) {
        return computeFallThrough(nextSibling);
      }
      if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
        return computeFallThrough(node.getNext()); // Fall through to the node after catch
      } else {
        return computeFollowNode(fromNode, node.getParent(), cfa); // Otherwise fall back to parent
      }
    } else if (parent.getLastChild() == node) { // If we are coming out of the FINALLY block...
      // ...
      // Ensure that control is transferred correctly after finally block.
      Node nextSibling = node.getNext();
      if (nextSibling != null && !nextSibling.isFunction()) {
        return computeFallThrough(nextSibling);
      }
      return computeFollowNode(fromNode, parent, cfa); // Otherwise fall back to parent
    }
  // ...
  
  // Now that we are done with the special cases follow should be its immediate sibling,
  // unless its sibling is a function or in the case of TRY/FINALLY where no proper node was found.
  Node nextSibling = node.getNext();
  
  // Skip function declarations because control doesn't get pass into it.
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    // If there are no more siblings, control is transferred up the AST.
    return computeFollowNode(fromNode, parent, cfa);
  }
}

// Assuming computeFallThrough(node) exists and is defined to handle the fall-through logic.