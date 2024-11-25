private void traverse(Node node) {
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  boolean changed = false;

  do {
    Node c = node.getFirstChild();
    while (c != null) {
      traverse(c);
      c = c.getNext(); // Fix: Update the reference to the next child, not dependent on an external 'next' variable.
    }

    visit(node);

    if (shouldRetraverse(node)) {
      changed = true; // Mark that there is a need to re-traverse this node
    } else {
      changed = false; // Reset flag after successful traversal
    }

    visits++;
    Preconditions.checkState(visits < 10000, "too many interations");
  } while (changed || shouldRetraverse(node));

  exitNode(node);
}