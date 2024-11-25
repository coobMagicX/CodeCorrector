private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Patch for assignments that appear in the init,
      // condition or iteration part of a FOR loop. Without
      // this change, all 3 of those parts try to claim the for
      // loop as their dependency scope. The last assignment in
      // those three fields wins, which can result in incorrect
      // reference edges between referenced and assigned variables.
      Node current = parent.getFirstChild();
      boolean isFirstOfLoopParts = true;
      while (current != null) {
        if (isFirstOfLoopParts && current == n) {
          recordDepScope(recordNode, ns);
          break; // Break after recording the first assignment of the loop parts
        }
        isFirstOfLoopParts = false;
        current = current.getNext();
      }
    } else {
      // The rhs of the assignment is the caller, so it's used by the
      // context. Don't associate it with the lhs.
      // Record dependency scope for the entire expression if it's not part of a loop or as simple statement.
      recordDepScope(recordNode, ns);
    }
  }
}