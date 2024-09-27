private void traverse(Node node) {
    // The goal here is to avoid retraversing
    // the entire AST to catch newly created opportunities.
    // So we track whether a "unit of code" has changed,
    // and revisit immediately.
    if (!shouldVisit(node)) {
        return;
    }

    int visits = 0;
    boolean shouldRetraverse;
    do {
        Node c = node.getFirstChild();
        while (c != null) {
            traverse(c);
            c = c.getNext();
        }

        visit(node);
        visits++;
        shouldRetraverse = shouldRetraverse(node);

        Preconditions.checkState(visits < 10000, "too many iterations");
    } while (shouldRetraverse);

    exitNode(node);
}