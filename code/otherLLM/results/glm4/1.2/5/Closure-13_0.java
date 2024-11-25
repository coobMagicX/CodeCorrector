private void traverse(Node node) {
  // The goal here is to avoid retraversing the entire AST to catch newly created opportunities.
  // So we track whether a "unit of code" has changed, and revisit immediately.

  if (!shouldVisit(node)) {
    return;
  }

  Set<Node> visited = new HashSet<>();
  Stack<Node> stack = new Stack<>();
  stack.push(node);

  while (!stack.isEmpty()) {
    Node current = stack.pop();
    if (visited.contains(current)) {
      continue; // Skip this node as it has already been processed
    }
    visited.add(current);

    // Traverse children and push them onto the stack
    Node c = current.getFirstChild();
    while (c != null) {
      stack.push(c);
      c = c.getNext();
    }

    visit(current);
  }

  exitNode(node);
}