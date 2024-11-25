private void traverse(Node node) {
  // The goal here is to avoid retraversing the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed, and revisit immediately.

  while (!shouldRetraverse(node)) {
    if (!shouldVisit(node)) {
      return;
    }

    int visits = 0;
    Node c = node.getFirstChild();
    while (c != null) {
      traverse(c);
      c = c.getNext();
    }

    visit(node);
    visits++;

    // Check for the number of iterations to prevent infinite loops
    if (visits >= 10000) {
      break; // Exit loop if too many iterations occur
    }

    node = node.getParent(); // Move up to parent after visiting children
  }

  exitNode(node);
}