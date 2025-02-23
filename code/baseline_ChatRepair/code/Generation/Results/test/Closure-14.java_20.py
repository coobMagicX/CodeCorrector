private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (node.isReturn() && !node.hasChildren()) || (cfa != null && node == cfa.root)) {
        // Handle the scenario of reaching the end of function or script without a return.
        if (parent != null && parent.isFunction() && parent.getFunctionReturnType() != null 
            && !parent.getFunctionReturnType().isVoidType() && !node.isReturn()) {
            // Log error or handle missing return according to your environment, like:
            System.err.println("JSC_MISSING_RETURN_STATEMENT. Expected return type: " + parent.getFunctionReturnType());
        }  
        return cfa.getSymbolicReturnNode(parent);
    }
  
    switch (parent.getType()) {
        case Token.IF:
            return computeFollowNode(fromNode, parent, cfa);
        // Add relevant case handling
    }

    // Handling other broader cases similarly...
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
