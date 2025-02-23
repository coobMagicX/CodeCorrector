public boolean apply(Node n) {
    if (n == null) {
        // Reached implicit return where the function returns possibly without a return statement
        return false;
    }

    // Specifically handle common side-effect patterns like deletion within for-in loops
    switch (n.getToken()) {
        case DELPROP:  // Node is a delete property operation
            return true;
        case FOR_IN:
            // FOR_IN loops should be considered for side-effects if it modifies global state
            if (modifiesState(n)) {
                return true;
            }
            break;
        case CALL:
            // Check for side effects in function calls
            if (NodeUtil.functionCallHasSideEffects(n)) {
                return true;
            }
            break;
        case NEW:
            // Check for side effects in constructor calls
            if (NodeUtil.constructorCallHasSideEffects(n)) {
                return true;
            }
            break;
        default:
            break;
    }

    // Recursively check child nodes
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (apply(child)) {
            return true;
        }
    }
    
    // If no side effects found, return false
    return false;
}

private boolean modifiesState(Node n) {
    // Example check that can be tuned depending on what state modification means
    // in your specific use case, like mutations to object or variable states.
    Node target = n.getSecondChild();
    return target != null && (target.isName() || target.isGetProp());
}
