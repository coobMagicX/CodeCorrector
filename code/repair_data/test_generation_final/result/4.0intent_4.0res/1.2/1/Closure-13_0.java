private void traverse(Node node) {
  // The goal here is to avoid retraversing
  // the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed,
  // and revisit immediately.
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  boolean shouldRevisit;
  do {
    shouldRevisit = false;
    Node c = node.getFirstChild();
    while(c != null) {
      traverse(c);
      Node next = c.getNext();
      c = next;
    }

    visit(node);
    visits++;

    // Report changes to track transformations and decide on re-traversal
    if (shouldRetraverse(node)) {
      reportChange();
      shouldRevisit = true;
    }

    Preconditions.checkState(visits < 10000, "too many iterations");
  } while (shouldRevisit);

  exitNode(node);
}