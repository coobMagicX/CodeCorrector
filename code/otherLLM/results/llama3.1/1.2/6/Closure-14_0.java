private static Node computeFallThrough(Node node) {
  if (node == null || !node.isFinally()) {
    throw new AssertionError("Expected a FINALLY block");
  }

  // If we are coming out of the FINALLY block...
  return computeFollowNode(null, node.getParent(), ControlFlowAnalysis.get(node));
}