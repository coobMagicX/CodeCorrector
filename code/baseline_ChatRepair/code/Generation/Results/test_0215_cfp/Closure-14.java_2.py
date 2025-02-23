private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
        return null;
    }

    // If the node is a return statement, handle according to its parent
    if (node.isReturn()) {
        // If within a function, it's likely the function should end after this node
        if (parent.isFunction()) {
            // The function ends here. Since return node itself stops execution, return control should ideally 
            // go to null to signify end of function scope
            return null;
        }
        // Additional checks to implement based on other structures if necessary
    }

    switch (parent.getType()) {
    case Token.IF:
        return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
        if (parent.getNext() != null) {
            if (parent.getNext().isCase()) {
                return parent.getNext().getFirstChild().getNext();
            } else if (parent.getNext().isDefaultCase()) {
                return parent.getNext().getFirstChild();
            } else {
                Preconditions.checkState(false, "Unreachable");
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
        if (parent.getFirstChild() == node) {
            if (NodeUtil.hasFinally(parent)) {
                return computeFallThrough(parent.getLastChild());
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }
        } else if (NodeUtil.getCatchBlock(parent) == node){
            if (NodeUtil.hasFinally(parent)) {
                return computeFallThrough(node.getNext());
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }
        } else if (parent.getLastChild() == node){
            if (cfa != null) {
                for (Node finallyNode : cfa.finallyMap.get(parent)) {
                    cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
                }
            }
            return computeFollowNode(fromNode, parent, cfa);
        }
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
}
