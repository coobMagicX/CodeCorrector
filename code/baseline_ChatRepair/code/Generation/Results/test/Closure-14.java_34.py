private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null) {
    return null;
  }

  if (parent.isFunction()) {
    // If the node is a return statement within a function, return the node itself, which ensures
    // correct transfer back to the caller of the function.
    if (node.isReturn()) {
      // This can possibly be connected to a symbolic return node or handled based on specific requirements.
      return node;
    } else {
      // If parent is a function but node is not a return, then proceed to examine other siblings or parent logic.
      return computeFollowNode(fromNode, parent, cfa);
    }
  }

  // If the node is root and we are considering end of script or transfer control
  if (cfa != null && node == cfa.root) {
    return null;
  }

  // Existing logic for handling different node types like IF, FOR, WHILE etc..
  ...

  // Exiting the function without adjusting current logic.
  return null;
}
