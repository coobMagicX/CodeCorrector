private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);
    if (ns != null) {
        if (parent.isFor() && !NodeUtil.isForIn(parent)) {
            // Patch for assignments that appear in the init,
            // condition or iteration part of a FOR loop. Without
            // this change, all 3 of those parts try to claim the for
            // loop as their dependency scope. The last assignment in
            // those three fields wins, which can result in incorrect
            // reference edges between referenced and assigned variables.
            //
            // TODO(user) revisit the dependency scope calculation
            // logic.
            if (parent.getFirstChild().getNext() != n) {
                recordDepScope(recordNode, ns);
            } else {
                recordDepScope(nameNode, ns);
            }
        } else {
            // The rhs of the assignment may be a complex expression including function calls or object literals.
            // If rhs includes a function expression, we need to ensure it captures the correct scope, especially
            // if it captures variables from an outer scope. This ensures dependencies are tracked correctly across
            // different scopes and function closures.
            Node rhs = nameNode.getNext();
            if (rhs != null && (rhs.isFunction() || rhs.isCall())) {
                // If the rhs is a function or a call expression, associate the rhs node as the dependency scope
                // to ensure that any variables it captures are linked to the correct context, capturing the broader scope.
                recordDepScope(rhs, ns);
            } else {
                // For other cases, continue to use the recordNode (generally the lhs) for dependency scope.
                recordDepScope(recordNode, ns);
            }
        }
    }
}