private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();

  if (parent == null || node.isReturn()) {
    // For return statements or top-level scripts, control transfers to a symbolic node or caller.
    if (node.isReturn() && cfa != null && parent.isFunction()) {
      return cfa.getSymbolicReturnNodeFromFunction(parent);
    } else {
      return null;  // Script level return or function end return.
    }
  }
  if (parent.isFunction()) {
    // When the parent is a function and the node isn't a return,
    // the normal exit of the function needs handling.
    if (cfa != null) {
      return cfa.getSymbolicReturnNodeFromFunction(parent);
    }
    return null;
  }

  // Existing code that handles various other node types...
  
  // After processing specific nodes, address remaining default condition.
  Node nextSibling = node.getNext();

  // Skip function declarations as control doesn't pass into them.
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    // For nodes that are the last in their block, transfer control to the parent.
    return computeFollowNode(fromNode, parent, cfa);
  }
}
