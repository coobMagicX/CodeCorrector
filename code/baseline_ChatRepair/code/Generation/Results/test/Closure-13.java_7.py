private void traverse(Node node) {
  if (!shouldVisit(node)) {
    return;
  }

  Set<Node> visitedNodes = new HashSet<>();
  do {
    // We use a queue to manage the nodes to be checked to prevent stack overflow
    Queue<Node> nodesToVisit = new LinkedList<>();
    nodesToVisit.add(node);

    while (!nodesToVisit.isEmpty()) {
      Node currentNode = nodesToVisit.poll();
      if (!visitedNodes.add(currentNode)) {
        continue; // Skip nodes that have been revisited without changes
      }

      for (Node c = currentNode.getFirstChild(); c != null; c = c.getNext()) {
        nodesToVisit.add(c);
      }

      visit(currentNode);
    }

    Preconditions.checkState(visitedNodes.size() < 10000, "too many iterations");
  } while (shouldRetraverse(node) && !visitedNodes.contains(node));
  
  exitNode(node);
}

/**
 * Assumption: `visit(node)` potentially modifies the tree triggering shouldRetraverse(node).
 * You might want to refine the shouldRetraverse condition, ensuring it’s correctly reset/based on actual data change.
 * Depending on how `visit(node)` operates, it should ideally set some state that clearly denotes
 * node modification state to coordinate reruns. 
 */
