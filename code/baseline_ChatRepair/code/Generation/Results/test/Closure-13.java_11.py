private void traverse(Node node) {
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  do {
    Node c = node.getFirstChild();
    while(c != null) {
      traverse(c);
      c = c.getNext();
    }

    // visit the node to possibly change it and require re-traversal
    visit(node);
    // Increment our counter to prevent indefinite traversal.
    visits++;

    Preconditions.checkState(visits < 10000, "too many iterations");
  } while (shouldRetraverse(node)); // Only rely on shouldRetraverse to control the loop.

  exitNode(node);
}
