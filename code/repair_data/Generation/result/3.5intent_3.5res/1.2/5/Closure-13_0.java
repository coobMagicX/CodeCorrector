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

    Preconditions.checkState(visits < 10000, "too many iterations");
    if (traversalState.peek().changed) {
      // Resetting the changed flag
      traversalState.peek().changed = false;
      // Retraverse the node
      continue;
    }
  } while (shouldRetraverse(node) || traversalState.peek().changed); // Add condition to re-traverse if change was reported

  exitNode(node);
}

public void reportChange() {
  traversalState.peek().changed = true;
}