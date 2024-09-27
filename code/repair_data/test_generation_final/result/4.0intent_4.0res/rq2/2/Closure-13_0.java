private void traverse(Node node) {
    // The goal here is to avoid retraversing
    // the entire AST to catch newly created opportunities.
    // So we track whether a "unit of code" has changed,
    // and revisit immediately.
    if (!shouldVisit(node)) {
        return;
    }

    int visits = 0;
    boolean changed;
    do {
        changed = false;
        Node c = node.getFirstChild();
        while (c != null) {
            traverse(c);
            Node next = c.getNext();
            c = next;
        }

        visit(node);
        if (traversalState.peek().changed) {
            changed = true;
            traversalState.peek().changed = false; // Reset the change flag after handling
            reportChange(); // Ensures that changes are properly reported and handled
        }
        visits++;

        Preconditions.checkState(visits < 10000, "too many iterations");
    } while (changed && shouldRetraverse(node));

    exitNode(node);
}