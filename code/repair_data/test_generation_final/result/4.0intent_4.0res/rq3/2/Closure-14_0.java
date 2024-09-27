private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.getRoot())) {
        return null; // In the described cases, return null to indicate no follow node.
    }

    // If we are just before an IF/WHILE/DO/FOR:
    switch (parent.getType()) {
        // The follow() of any of the path from IF would be what follows IF.
        case Token.IF:
            return computeFollowNode(fromNode, parent, cfa);

        case Token.CASE:
        case Token.DEFAULT_CASE:
            // After the body of a CASE, the control goes to the body of the next
            // case, without having to go to the case condition.
            if (parent.getNext() != null) {
                if (parent.getNext().isCase() || parent.getNext().isDefaultCase()) {
                    return parent.getNext().getFirstChild();
                } else {
                    throw new IllegalStateException("Not reachable");
                }
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }

        case Token.FOR:
            if (NodeUtil.isForIn(parent)) {
                return parent;
            } else {
                return parent.getFirstChild().getNext().getNext();
            }

        case Token.WHILE:
        case Token.DO:
            return parent;

        case Token.TRY:
            // If we are coming out of the TRY block...
            if (parent.getFirstChild() == node) {
                if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
                    return parent.getLastChild();
                } else { // and have no FINALLY.
                    return computeFollowNode(fromNode, parent, cfa);
                }
            // CATCH block.
            } else if (NodeUtil.getCatchBlock(parent) == node) {
                if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
                    return node.getNext();
                } else {
                    return computeFollowNode(fromNode, parent, cfa);
                }
            // If we are coming out of the FINALLY block...
            } else if (parent.getLastChild() == node) {
                if (cfa != null) {
                    for (Node finallyNode : cfa.getFinallyMap().get(parent)) {
                        cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
                    }
                }
                return computeFollowNode(fromNode, parent, cfa);
            }
    }

    // Now that we are done with the special cases follow should be its
    // immediate sibling, unless its sibling is a function
    Node nextSibling = node.getNext();

    // Skip function declarations because control doesn't get passed into it.
    while (nextSibling != null && nextSibling.isFunction()) {
        nextSibling = nextSibling.getNext();
    }

    if (nextSibling != null) {
        return nextSibling;
    } else {
        // If there are no more siblings, control is transferred up the AST.
        return computeFollowNode(fromNode, parent, cfa);
    }
}