private void traverse(Node node) {
    // The goal here is to avoid retraversing the entire AST to catch newly created opportunities.
    // So we track whether a "unit of code" has changed, and revisit immediately.

    if (!shouldVisit(node)) {
        return;
    }

    int visits = 0;
    boolean shouldContinue = true;

    while (shouldContinue && visits < 10000) { // Ensure the loop will terminate after too many iterations
        Node c = node.getFirstChild();
        shouldContinue = false; // Reset flag, assuming no changes

        while(c != null) {
            traverse(c);
            c = c.getNext(); // Corrected from 'c' to 'c.getNext()' to move to the next sibling
        }

        if (visit(node)) { // Assuming visit() returns true if it made a change, or false otherwise
            shouldContinue = true; // If there was a change, we should continue the loop
        }
        
        visits++;
    }

    exitNode(node);
}