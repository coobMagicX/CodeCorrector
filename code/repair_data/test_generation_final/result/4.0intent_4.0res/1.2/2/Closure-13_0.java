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
        visits++;
        
        // Check if the node has changed during the visit,
        // if so, set the flag to ensure a retraverse if necessary.
        if (traversalState.peek().changed) {
            changed = true;
            reportChange(); // Ensure the change is recorded to manage traversal state effectively.
            traversalState.peek().changed = false; // Reset change state after recording it.
        }

        Preconditions.checkState(visits < 10000, "too many iterations");
    } while (changed); // Use the local 'changed' flag to control the loop.

    exitNode(node);
}