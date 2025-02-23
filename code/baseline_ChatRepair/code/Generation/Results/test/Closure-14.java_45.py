private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
      (cfa != null && node == cfa.root)) {
    return null;  // Here modifications might be necessary to handle returns properly.
  }

  switch (parent.getType()) {
    case Token.RETURN:
      if (cfa != null && cfa.finallyMap.containsKey(parent)) {
        // If there is a finally block, transfer control to the finally block
        Node finallyNode = cfa.finallyMap.get(parent).getFirstChild();
        return computeFallThrough(finallyNode);
      } else {
        // Return should go back to the caller or handle the symbolic return node
        return null; // adjust this according to how you handle exits from functions or scopes.
      }
    // other cases...
  }

  // other existing logic...
}
