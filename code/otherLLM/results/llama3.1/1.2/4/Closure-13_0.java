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

    // Check if we've reached the maximum iterations
    Preconditions.checkState(visits < 10000, "too many iterations");

    // If we're not retraversing and there are no circular references,
    // we can safely break out of the loop
    if (!shouldRetraverse(node) && !hasCircularReference(node)) {
      break;
    }
  } while (true);

  exitNode(node);
}

// Helper method to check for circular references
private boolean hasCircularReference(Node node) {
  Set<Node> visited = new HashSet<>();
  return traverseForCircularReferences(node, visited);
}

private boolean traverseForCircularReferences(Node node, Set<Node> visited) {
  if (node == null || visited.contains(node)) {
    return false;
  }
  visited.add(node);
  
  // Traverse the children of the current node
  Node c = node.getFirstChild();
  while(c != null) {
    if (traverseForCircularReferences(c, visited)) {
      return true;
    }
    Node next = c.getNext();
    c = next;
  }

  return false;
}