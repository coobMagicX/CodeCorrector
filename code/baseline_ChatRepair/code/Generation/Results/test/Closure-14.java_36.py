private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction()) {
        if (!node.isReturn()) {
            throw new RuntimeException("JSC_MISSING_RETURN_STATEMENT. Missing return statement. Function expected to return number.");
        }
        return null;  // Assuming the return node properly handles the return.
    }

    switch (parent.getType()) {
        case Token.IF:
        case Token.FOR:
        case Token.WHILE:
        case Token.DO:
        case Token.TRY:
            // Handle control structures - just an example, fill out with actual cases
            return handleControlStructure(parent, node, cfa);
        case Token.CASE:
        case Token.DEFAULT_CASE:
            // handle case blocks
            return handleCaseBlocks(parent, node);
        default:
            break;
    }

    if (node.getNext() != null) {
        return node.getNext();  // proceed to next sibling
    } else if (parent != null) {
        return computeFollowNode(fromNode, parent, cfa);  // recursive call to handle up the tree
    }

    return null;  // default case if no conditions met
}

private static Node handleControlStructure(Node parent, Node node, ControlFlowAnalysis cfa) {
    // Specified handling of particular control structures
    // Placeholder for actual implementation
    return null;
}

private static Node handleCaseBlocks(Node parent, Node node) {
    // Specified handling of case blocks
    // Placeholder for actual implementation
    return null;
}
