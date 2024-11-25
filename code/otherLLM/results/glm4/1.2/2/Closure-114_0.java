private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Determine the correct scope for assignments in the init,
      // condition or iteration part of a FOR loop.
      Node firstChildOfParent = parent.getFirstChild();
      Node nextSibling = n.getNextSibling();

      // Record dependency scope based on the position of 'n' within the for loop structure
      if (firstChildOfParent.getNext() == n) {
        // This is the assignment in the init part of the FOR loop.
        recordDepScope(recordNode, ns);
      } else if ((nextSibling != null && firstChildOfParent.getNext().isForInit()) ||
                 (nextSibling == null && parent.isForCondition())) {
        // This is the assignment in the condition or iteration part of the FOR loop.
        recordDepScope(nameNode, ns);
      } else {
        // Otherwise, use the standard dependency recording method
        recordDepScope(recordNode, ns);
      }
    } else {
      // The rhs of the assignment is the caller, so it's used by the context. Don't associate it w/ the lhs.
      recordDepScope(recordNode, ns);
    }
  }
}