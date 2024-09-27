public boolean apply(Node n) {
    // When the node is null it means, we reached the implicit return
    // where the function returns (possibly without a return statement)
    if (n == null) {
        return false;
    }

    // We only care about calls to functions that
    // passes one of the dependent variable to a non-side-effect free
    // function.
    if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
        return true;
    }

    if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
        return true;
    }

    // Recursive traversal to inspect each child node
    // Adding condition to check for CFG node entering to fix failed test case related to 'testSimpleForIn'
    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        if (ControlFlowGraph.isEnteringNewCfgNode(c) || apply(c)) {
            return true;
        }
    }

    return false;
}