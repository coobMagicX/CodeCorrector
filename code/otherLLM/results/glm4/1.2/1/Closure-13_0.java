private void traverse(Node node) {
  // The goal here is to avoid retraversing
  // the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed,
  // and revisit immediately.
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  boolean hasChanged = false; // Flag to indicate if any child was visited
  do {
    Node c = node.getFirstChild();
    while (c != null) {
      traverse(c);
      Node next = c.getNext();
      c = next;
    }

    visit(node); // Visit the current node after all children are traversed
    hasChanged |= shouldRetraverse(node); // Update flag if the node needs to be retraversed

    visits++;
    Preconditions.checkState(visits < 10000, "too many iterations");
  } while (hasChanged && !shouldStopTraversal());

  exitNode(node);
}

// Helper method to determine if traversal should stop
private boolean shouldStopTraversal() {
  // Assuming the intention is that we should stop if no more changes are needed.
  return !hasChanged;
}