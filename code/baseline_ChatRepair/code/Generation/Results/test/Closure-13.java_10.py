private void traverse(Node node) {
  if (!shouldVisit(node)) {
    return;
  }

  boolean changed;
  int visits = 0;
  do {
    changed = false;
    Node child = node.getFirstChild();
    while (child != null) {
      traverse(child);
      child = child.getNext();
    }

    if (visit(node)) {
      // Assuming visit() now returns a boolean indicating if it changed the node in a way that requires rechecking.
      changed = true;
    }
    visits++;

    Preconditions.checkState(visits < 10000, "too many iterations");
  } while (changed && shouldRetraverse(node)); // Check both changed and shouldRetraverse to proceed cautiously

  exitNode(node);
}
