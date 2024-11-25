private void traverse(Node node) {
  if (!shouldVisit(node)) {
    return;
  }

  boolean hasChanged = true;
  while (hasChanged || shouldRetraverse(node)) {
    hasChanged = false; // Reset flag for this iteration

    Node c = node.getFirstChild();
    while(c != null) {
      traverse(c);
      Node next = c.getNext();
      if (c.hasChanged()) { // Assume a method 'hasChanged' exists to check if the node has changed
        hasChanged = true;
      }
      c = next;
    }

    visit(node);

    if (node.hasChanged() || shouldRetraverse(node)) { // Check again before next iteration
      hasChanged = true;
    }

    Preconditions.checkState(visits < 10000, "too many interations");
  }

  exitNode(node);
}