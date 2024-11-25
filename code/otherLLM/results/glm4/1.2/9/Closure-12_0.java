private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode == null) {
        return false;
    }
    
    // Check for a try block
    if (cfgNode instanceof TryStatement) {
        return true;
    }
    
    // If the node is a catch block, it should be inside a try block
    if (cfgNode instanceof CatchClause) {
        TryStatement parentTry = findParentTry((CatchClause) cfgNode);
        return parentTry != null;
    }
    
    // Check for other control flow constructs that may require exception handling
    // This is an example and should be expanded based on actual node types that may need exception handling
    if (cfgNode instanceof IfStatement || 
        cfgNode instanceof SwitchStatement ||
        cfgNode instanceof WhileStatement ||
        cfgNode instanceof DoWhileStatement) {
        return true;
    }
    
    // Recursively check for children nodes
    List<Node> children = cfgNode.getChildNodes();
    for (Node child : children) {
        if (hasExceptionHandler(child)) {
            return true;
        }
    }
    
    return false;
}

// Helper method to find the parent TryStatement of a given CatchClause
private TryStatement findParentTry(CatchClause catchClause) {
    Node current = catchClause.getParent();
    while (current != null) {
        if (current instanceof TryStatement) {
            return (TryStatement) current;
        }
        current = current.getParent();
    }
    return null;
}