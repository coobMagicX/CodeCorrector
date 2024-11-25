private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    // Check if the node is a name and the variable matches.
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        // If the node is the LHS of its parent, it's potentially killed after RHS is evaluated.
        if (NodeUtil.isLhs(n, n.getParent())) {
            return VariableLiveness.KILL;
        }
        // Otherwise, the variable is read.
        else {
            return VariableLiveness.READ;
        }
    }

    // Process all children nodes for potential reads or kills.
    for (Node child = n.getFirstChild();
         child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION node
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            if (state == VariableLiveness.KILL || state == VariableLiveness.READ) {
                // If we find a KILL or READ, return it immediately.
                return state;
            }
        }
    }

    // If no kill was found, the variable may be live.
    return VariableLiveness.MAYBE_LIVE;
}