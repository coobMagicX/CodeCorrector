private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    // Determine the dependency scope for assignments within loops or function expressions
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      Node loopInitNode = parent.getFirstChild();
      Node loopConditionNode = parent.getChild(2);
      Node loopIterNode = parent.getLastChild();

      boolean isCurrentNodeLoopPart = false;
      if (loopInitNode != null && loopInitNode.getNext() == n) {
        isCurrentNodeLoopPart = true;
      } else if (parent.getFirstChild().getNext() != n && (n.equals(loopConditionNode) || n.equals(loopIterNode))) {
        isCurrentNodeLoopPart = true;
      }

      if (isCurrentNodeLoopPart) {
        recordDepScope(recordNode, ns);
      } else {
        // Handle other cases such as assignments not within the loop parts
        recordDepScope(nameNode, ns);
      }
    } else {
      // For other than loops or function expressions
      recordDepScope(recordNode, ns);
    }
  }
}