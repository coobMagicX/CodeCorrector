private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);
    if (ns != null) {
        // Determine the correct dependency scope for assignments within a FOR loop
        if (parent.isFor() && !NodeUtil.isForIn(parent)) {
            // Only record the dependency scope for the assignment node in the init or condition part of the FOR loop
            if (isLoopInitOrCondition(n, parent)) {
                recordDepScope(recordNode, ns);
            }
        } else {
            // For other cases, ensure that we are not recording incorrect dependencies with outer variables
            if (!isNestedCallExpression(n)) {
                recordDepScope(recordNode, ns);
            }
        }
    }
}

private boolean isLoopInitOrCondition(Node n, Node parent) {
    return (parent.isFor() &&
            ((n == parent.getFirstChild()) || // For loop init
             (n == parent.getFirstChild().getNext() && parent.getFirstChild().getNext().getNext() != null && parent.getFirstChild().getNext().getNext().isConditionalExpression()))); // For loop condition
}

private boolean isNestedCallExpression(Node n) {
    Node current = n;
    while (current != null) {
        if (current.isFunctionCall()) {
            return true;
        }
        current = current.getParent();
    }
    return false;
}