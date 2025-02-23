private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();

  // Case handling when there is no parent or the node is part of a function or it is a return statement  
  if (parent == null || parent.isFunction() || node.isReturn()) {
    // For nodes with no parent, nodes which are functions themselves, or return statements,
    // control should return to the caller or end the script, both of which are represented by `null`.
    return null;  
  }

  // If the node is the root node in the Control Flow Analysis
  if (cfa != null && node == cfa.root) {
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
