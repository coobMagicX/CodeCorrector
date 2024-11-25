private void traverse(Node node) {
  // The goal here is to avoid retraversing
  // the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed,
  // and revisit immediately.
  if (!shouldVisit(node)) {
    return;
  }

  boolean shouldRevisit = true; // Flag to indicate if node needs revisiting
  while (shouldRevisit) { // Use a while loop instead of do-while for better control
    Node c = node.getFirstChild();
    while(c != null) {
      traverse(c);
      Node next = c.getNext();
      c = next;
    }

    visit(node);
    shouldRevisit = shouldRetraverse(node); // Update the flag based on retraversal condition

    Preconditions.checkState(shouldRevisit == false || visits < 10000, "too many iterations");
    visits++;
  } 

  exitNode(node);
}