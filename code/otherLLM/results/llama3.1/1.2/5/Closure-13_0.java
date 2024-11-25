private void traverse(Node node) {
  // The goal here is to avoid retraversing
  // the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed,
  // and revisit immediately.
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  do {
    Node c = node.getFirstChild();
    while(c != null) {
      traverse(c);
      Node next = c.getNext();
      c = next;
    }

    visit(node);
    visits++;

    Preconditions.checkState(visits < 10000, "too many interations");
  } while (!shouldRetraverseImmediately(node)); // Modify the loop condition

  exitNode(node);
}

private boolean shouldRetraverseImmediately(Node node) {
  return traversalState.peek().traverseChildScopes && hasOpportunityChanged(node); // New method to check if opportunity changed
}

private boolean hasOpportunityChanged(Node node) {
  // This is a placeholder for the actual logic to determine if an opportunity has changed.
  // The intent here is to validate retraversal criteria as per the repair strategy.
  return false; // Modify according to your actual implementation
}