private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();

  // Check if the node itself is a return statement or we're at an exit point of the control flow.
  // If it is, and the parent is not null, we might need to check if the parent is function or script root.
  if (node.isReturn()) {
    if (parent != null && parent.isFunction()) {
      // Terminate the flow for this function as return exits the function.
      return null;
    }
  }

  if (parent == null || parent.isFunction()) {
    // Existing handling for null parent or parent that is a function.
    return null;
  }

  if (cfa != null && node == cfa.root) {
    // Handling for reaching root in analysis.
    return null;
  }

  // Existing handling for if, for, while, do, try, and others continuation in loops and conditionals.
  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    // Additional cases here remain unchanged.
  }

  // Continue with unchanged code to further navigate the structure depending on cases.
}
