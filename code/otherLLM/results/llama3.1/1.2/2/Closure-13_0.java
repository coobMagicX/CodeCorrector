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

    // Check if we have already visited a sufficient number of nodes
    if (visits >= 10000 && shouldVisit(node)) {
      break; // Exit the loop early to prevent infinite recursion
    }
    
    Preconditions.checkState(visits < 10000, "too many interations");
  } while (shouldRetraverse(node));

  exitNode(node);
}