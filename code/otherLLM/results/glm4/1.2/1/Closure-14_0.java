private static Node computeFollowNode(
        Node fromNode, Node node, ControlFlowAnalysis cfa) {
    // ...
    case Token.TRY:
        if (parent.getFirstChild() == node) {
            if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
                return computeFallThrough(parent.getLastChild());
            } else { // and have no FINALLY.
                Node next = parent.getNext();
                if (next != null && !next.isFunction()) {
                    return computeFollowNode(fromNode, next, cfa);
                }
            }
        }
        // CATCH block.
        else if (NodeUtil.getCatchBlock(parent) == node) {
            Node catchNext = NodeUtil.getCatchBlockParent(node);
            if (catchNext != null && !catchNext.isFunction()) {
                return computeFollowNode(fromNode, catchNext, cfa);
            }
            // If we are coming out of the FINALLY block...
        } else if (parent.getLastChild() == node) {
            Node next = node.getNext();
            if (next != null && !next.isFunction()) {
                for (Node finallyNode : cfa.finallyMap.get(parent)) {
                    cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
                }
                return computeFollowNode(fromNode, next, cfa);
            }
        }
    // ...
}