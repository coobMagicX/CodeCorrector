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
            Node next = c.getNextSibling(); // Fixed from c.getNext() to c.getNextSibling()
            traverse(c);
            c = next;
        }

        visit(node);
        visits++;

        Preconditions.checkState(visits < 10000, "too many iterations"); // Fixed typo in "interations" to "iterations"
    } while (shouldRetraverse(node));

    exitNode(node);
}