private void traverse(Node node) {
  // The goal here is to avoid retraversing
  // the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed,
  // and revisit immediately.

  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  boolean needToRetrace = true;

  while (needToRetrace) {
    Node c = node.getFirstChild();
    boolean visitedChildren = false;

    while (c != null && !visitedChildren) {
      traverse(c);
      c = c.getNext(); // Assuming this is the correct method to get the next sibling
      if (c != null) {
        visitedChildren = true;
      }
    }

    if (shouldRetrace(node)) { // Renamed condition to avoid confusion with 'shouldVisit'
      visit(node);
      visits++;

      Preconditions.checkState(visits < 10000, "too many iterations");
    } else {
      needToRetrace = false; // Stop re-traversing when no more changes are detected
    }

    node = node.getParent(); // Move to the parent node if needed for further traversal

    // Check if there's a need to retrace up the tree or stop at this level
    if (node != null && shouldRetrace(node)) {
      needToRetrace = true;
    } else {
      needToRetrace = false;
    }
  }

  exitNode(node);
}