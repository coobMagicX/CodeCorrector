private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
        return null; // This return should potentially be revised if function is expected to return a Node.
    }

    switch (parent.getType()) {
        case Token.IF:
            return computeFollowNode(fromNode, parent, cfa);
        // Add other cases as already defined in your original code
    }

    Node nextSibling = node.getNext();
    while (nextSibling != null && nextSibling.isFunction()) {
        nextSibling = nextSibling.getNext();
    }

    if (nextSibling != null) {
        return computeFallThrough(nextSibling);
    } else {
        return computeFollowNode(fromNode, parent, cfa);
    }

    // Add a default return statement that provides a reasonable default Node if suitable, or throw an exception if the situation should not occur
    throw new IllegalStateException("Unhandled case in computeFollowNode");
}
