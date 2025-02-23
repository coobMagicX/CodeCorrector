private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
        return null; // When there's no parent, or the parent is a function, or the node is the root in CFA.
    }

    switch (parent.getType()) {
        case Token.IF:
        case Token.FOR:
        case Token.WHILE:
        case Token.DO:
        case Token.TRY:
            return computeFollowNode(fromNode, parent, cfa);

        case Token.CASE:
        case Token.DEFAULT_CASE:
            if (parent.getNext() != null) {
                return computeCaseNext(parent);
            }
            return computeFollowNode(fromNode, parent, cfa);

        default:
            // Handle sibling nodes and possible fall through
            Node nextSibling = node.getNext();
            while (nextSibling != null && nextSibling.isFunction()) { 
                nextSibling = nextSibling.getNext(); // Skip function declarations.
            }

            if (nextSibling != null) {
                return computeFallThrough(nextSibling);
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }
    }
    // Should not reach this point as all conditions handled, but required to satisfy the compiler.
    throw new IllegalStateException("Unhandled case in computeFollowNode");
}

private static Node computeCaseNext(Node parent) {
    Node next = parent.getNext();
    if (next.isCase()) {
        return next.getFirstChild().getNext();
    } else if (next.isDefaultCase()) {
        return next.getFirstChild();
    } else {
        throw new IllegalStateException("Expected a CASE or DEFAULT_CASE but found: " + next.getType());
    }
}
