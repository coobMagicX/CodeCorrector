private void traverse(Node node) {
  if (!shouldVisit(node)) {
    return;
  }

  boolean shouldContinue = true;
  while (shouldContinue) {
    Node c = node.getFirstChild();
    List<Node> childrenToRevisit = new ArrayList<>();

    while (c != null) {
      // Store nodes that need to be revisited due to changes
      if (shouldRetraverse(c)) {
        childrenToRevisit.add(c);
      }
      traverse(c);
      c = c.getNext();
    }

    visit(node);

    // Re-traverse any children that needed re-visiting
    for (Node child : childrenToRevisit) {
      traverse(child);
    }

    shouldContinue = !childrenToRevisit.isEmpty();

    Preconditions.checkState(shouldContinue || node.isLeaf(), "too many iterations or non-leaf node in the loop");
  }

  exitNode(node);
}