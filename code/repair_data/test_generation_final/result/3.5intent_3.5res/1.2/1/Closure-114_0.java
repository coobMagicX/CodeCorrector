private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);
    if (ns != null) {
        if (n.isFunction() || NodeUtil.isFunctionExpression(n)) {
            // Handle assignments within function expressions
            recordDepScope(recordNode, ns);
        } else if (parent.isFor() && !NodeUtil.isForIn(parent)) {
            if (parent.getFirstChild().getNext() != n) {
                recordDepScope(recordNode, ns);
            } else {
                recordDepScope(nameNode, ns);
            }
        } else {
            recordDepScope(recordNode, ns);
        }
    }
}