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
    if (traversalState.peek().changed) {
      break; // Exit the loop if a change was reported
    }
  } while (shouldRetraverse(node));

  exitNode(node);
}

public void reportChange() {
  traversalState.peek().changed = true;
}