private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
      node.getType() == Token.RETURN ||  // Add this to handle return statements properly
      (cfa != null && node == cfa.root)) {
    return null;
  }

  ...
}
