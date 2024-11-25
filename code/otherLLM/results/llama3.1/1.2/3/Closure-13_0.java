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

    // Invalidate cached AST nodes and re-traverse affected subtrees
    if (hasNodeChanged(node)) {
      invalidateCachedNodes(node);
      re TraverseAffectedSubtrees(node);
    }
  } while (shouldRetraverse(node));

  exitNode(node);
}

// New methods added to the context
private boolean hasNodeChanged(Node node) {
  // implementation of this method is not provided in the context
}

private void invalidateCachedNodes(Node node) {
  // implementation of this method is not provided in the context
}

private void re TraverseAffectedSubtrees(Node node) {
  // implementation of this method is not provided in the context
}