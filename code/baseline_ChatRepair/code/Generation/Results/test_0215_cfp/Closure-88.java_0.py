private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    VariableLiveness result = VariableLiveness.MAYBE_LIVE; // Default state

    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            // This is a write, might still need to check other usages before declaring KILL
            result = VariableLiveness.KILL;
        } else {
            // This is a read
            return VariableLiveness.READ;
        }
    }

    // Recursively checking children nodes.
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
            VariableLiveness state = isVariableReadBeforeKill(child, variable);

            // If a read is found deeper in the tree, return immediately.
            if (state == VariableLiveness.READ) {
                return VariableLiveness.READ;
            }

            // If a KILL state is found, keep it if we haven't found a READ yet.
            if (state == VariableLiveness.KILL && result != VariableLiveness.READ) {
                result = VariableLiveness.KILL;
            }
        }
    }

    return result; // Return the most relevant state found
}
