public boolean apply(Node n) {
    // When the node is null it means, we reached the implicit return
    // where the function returns (possibly without a return statement)
    if (n == null) {
        return false;
    }

    // Check for function calls that pass a dependent variable to non-side-effect-free functions
    if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
        return true;
    }

    // Check for constructor calls that have side effects
    if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
        return true;
    }

    // Check for delete statements that have side effects
    if (n.isDelete() && NodeUtil.deleteHasSideEffects(n)) {
        return true;
    }

    // Recursively apply the check to child nodes
    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
            return true;
        }
    }
    return false;
}