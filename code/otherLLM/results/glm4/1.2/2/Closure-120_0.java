boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    Set<Node> visitedNodes = new HashSet<>();
    boolean isInLoop = false;

    // Helper method to check for loops recursively
    boolean visitNode(Node node, Node parent) {
        if (node == null || visitedNodes.contains(node)) {
            return true; // Already visited or is a boundary condition
        }
        visitedNodes.add(node);

        if (isBlockBoundary(node, parent)) {
            if (visitedNodes.size() > 1) { // More than one node visited in the loop
                isInLoop = true;
            }
            return false;
        }

        for (Node child : node.getChildren()) {
            if (visitNode(child, node)) {
                break; // Found a loop or boundary condition, no need to continue
            }
        }
        return false;
    }

    // Make sure this assignment is not in a loop.
    Node current = ref.getBasicBlock();
    while (current != null) {
        if (isInLoop) {
            break;
        }

        if (visitNode(current, current.getParent())) {
            isInLoop = true;
            break;
        }
        current = current.getParent();
    }

    return !isInLoop;
}