private void traverse(Node node) {
  if (!shouldVisit(node)) {
    return;
  }

  int visits = 0;
  boolean keepTraversing;
  do {
    keepTraversing = false;
    Node child = node.getFirstChild();
    while (child != null) {
      traverse(child);
      child = child.getNext();
    }

    visit(node);

    if (shouldRetraverse(node)) {
      keepTraversing = true;
    }

    visits++;
    Preconditions.checkState(visits < 10000, "too many iterations");
  } while (keepTraversing);
  // Note: Previously missing, including the exit condition clearly.
  exitNode(node);
}

/**
 * Make sure that shouldRetraverse(node) accurately represents whether changes
 * have been made that would require retraversing, possibly by having `visit`
 * update some state of the `node` or otherwise ensuring it only returns true
 * if relevant changes occurred since the last traversal.
 */
