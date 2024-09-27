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
        while (c != null) {
            traverse(c);
            c = c.getNext(); // Directly update 'c' to its next sibling
        }

        visit(node);
        visits++;

        // Ensuring that the state check for excessive iterations is accurate
        Preconditions.checkState(visits < 10000, "too many iterations");
    } while (shouldRetraverse(node) && visits < 10000); // Additional check to prevent unnecessary retraversal if visits limit is reached

    exitNode(node);
}