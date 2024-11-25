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
  } while (shouldRetraverse(node));

  // Check the reason for retraversal
  if (!shouldVisit(node) && shouldRetraverse(node)) {
    // If node hasn't changed and still needs to be retraversed,
    // it might lead to an infinite loop. Exit the loop instead.
    break;
  }

  exitNode(node);
}