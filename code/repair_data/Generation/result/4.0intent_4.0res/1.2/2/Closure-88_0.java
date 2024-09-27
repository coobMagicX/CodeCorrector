private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        // Check if the node is on the LHS of an assignment
        if (NodeUtil.isLhs(n, n.getParent())) {
            // Check if the parent is an ASSIGN node and if this node is not the last evaluated
            if (n.getParent() != null && n.getParent().isAssign() && n.getParent().getSecondChild() != n) {
                return VariableLiveness.READ; // Read before the assignment
            } else {
                return VariableLiveness.KILL; // The variable is killed here (i.e., it is being reassigned)
            }
        } else {
            return VariableLiveness.READ; // The variable is read
        }
    }

    // Recursive check in child nodes considering left to right, depth-first evaluation
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not entering a new control flow graph node
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            if (state != VariableLiveness.MAYBE_LIVE) {
                return state; // Return the state if it is either KILL or READ
            }
        }
    }
    return VariableLiveness.MAYBE_LIVE; // Return MAYBE_LIVE if no definitive state (KILL or READ) was found
}