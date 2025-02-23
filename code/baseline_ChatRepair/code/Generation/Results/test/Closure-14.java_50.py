private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();

    // Check for cases where control should transfer to the end of the script, back to the caller or when the node is root.
    if (parent == null || parent.isFunction()) {
        return null; // Transfer control to a "symbolic return" node or end of script/caller.
    } else if (cfa != null && node == cfa.root) {
        return null; // Specific check if the node under analysis is the root in the control flow analysis.
    }

    // IF/WHILE/DO/FOR and similar control structures handling. 
    switch (parent.getType()) {
        case Token.IF:
            return computeFollowNode(fromNode, parent, cfa);
        case Token.CASE:
        case Token.DEFAULT_CASE:
            // Control flow for case statements
            if (parent.getNext() != null) {
                if (parent.getNext().isCase() || parent.getNext().isDefaultCase()) {
                    return parent.getNext().getFirstChild();
                } else {
                    throw new IllegalStateException("Unexpected node type following CASE/DEFAULT_CASE");
                }
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }
        case Token.FOR:
            // Different handling for FOR and FOR-IN
            if (NodeUtil.isForIn(parent)) {
                return parent;
            } else {
                return parent.getFirstChild().getNext().getNext();
            }
        case Token.WHILE:
        case Token.DO:
            return parent;
        case Token.TRY:
            // Exception handling blocks
            if (parent.getFirstChild() == node && NodeUtil.hasFinally(parent)) {
                // If coming out of TRY and has FINALLY
                return parent.getLastChild();
            } else {
                // Coming out of CATCH or no FINALLY block
                return computeFollowNode(fromNode, parent, cfa);
            }
    }

    // Move to next sibling if not entering or exiting control structures or functions
    Node nextSibling = node.getNext();
    while (nextSibling != null && nextSibling.isFunction()) {
        nextSibling = nextSibling.getNext();
    }

    if (nextSibling != null) {
        return computeFallThrough(nextSibling);
    } else {
        // No more siblings, control is transferred up the AST.
        return computeFollowNode(fromNode, parent, cfa);
    }
}

private static Node computeFallThrough(Node node) {
    // Assuming computeFallThrough is properly defined elsewhere to handle sequential nodes.
    return node; // Placeholder implementation.
}
