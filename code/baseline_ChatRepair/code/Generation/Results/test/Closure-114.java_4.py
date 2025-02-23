private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);

    if (ns != null) {
        // Walk up the parent nodes to check if this assignment is part of a complex expression
        // like a parameter in a function call.
        Node current = parent;
        while (true) {
            if (current.isCall()) {
                // If assignment is within a function call, treat the function call as the dependent scope.
                recordDepScope(current, ns);
                return;
            } else if (current.isScript() || current.isFunction()) {
                // Stop at function or script root.
                break;
            }
            current = current.getParent();
            if (current == null) break;
        }

        // Default case: Record dependency scope normally if no special conditions are met.
        recordDepScope(recordNode, ns);
    }
}
