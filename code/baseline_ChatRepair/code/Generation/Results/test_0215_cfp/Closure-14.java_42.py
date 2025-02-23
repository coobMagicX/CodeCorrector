// If the node is a return statement, we should transfer control back to the caller
// this might not be perfectly represented without the global function context
case Token.RETURN:
    if(cfa != null && parent.isFunction()) {
        Node exitNode = cfa.getExitingNode(parent);
        if (exitNode != null) {
            return exitNode; // Return the exit node that represents returning from the function.
        }
    }
    return computeFollowNode(fromNode, parent, cfa); // Otherwise, continue to compute up the tree
