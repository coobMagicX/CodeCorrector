private void traverse(Node node) {
  // The goal here is to avoid retraversing
  // the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed,
  // and revisit immediately.
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  boolean hasChanged;
  do {
    Node c = node.getFirstChild();
    while (c != null) {
      traverse(c);
      c = c.getNext();
    }

    hasChanged = visit(node);
    visits++;

    if (visits >= 10000) {
      // Note: typically you wouldn't want to throw an exception like this in production;
      // instead, consider logging this condition or handling it gracefully as needed.
      throw new IllegalStateException("Too many iterations");
    }
  } while (hasChanged && shouldRetraverse(node));

  exitNode(node);
}

private boolean visit(Node node) {
  // Implement your visit logic here, returning true if the Node was changed
  // and false otherwise.
  
  // Placeholder for visit implementation:
  // return updateNodeIfNeeded(node);
  return false; // This should be replaced with actual logic.
}
