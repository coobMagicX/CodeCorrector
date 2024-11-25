private void traverse(Node node) {
  // The goal here is to avoid retraversing
  // the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed,
  // and revisit immediately.

  Set<Node> visited = new HashSet<>();
  
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  do {
    if (visited.contains(node)) {
      // If the node has already been visited, exit the method
      return;
    }
    visited.add(node); // Mark this node as visited

    Node c = node.getFirstChild();
    while(c != null) {
      traverse(c);
      c = c.getNext(); // Use the correct method to get the next sibling
    }

    visit(node);
    visits++;

    Preconditions.checkState(visits < 10000, "too many interations");
  } while (shouldRetraverse(node));

  exitNode(node);
}