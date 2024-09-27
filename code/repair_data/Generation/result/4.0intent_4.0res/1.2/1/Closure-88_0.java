private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            // Change here: After an assignment (KILL), check if the variable is used (READ) in the same expression.
            // This is especially important in complex expression where the variable might be reassigned and used.
            Node parent = n.getParent();
            if (parent != null && (parent.isOr() || parent.isAnd())) {
                VariableLiveness siblingState = isVariableReadBeforeKill(parent, variable);
                if (siblingState == VariableLiveness.READ) {
                    return VariableLiveness.READ;
                }
            }
            return VariableLiveness.KILL;
        } else {
            return VariableLiveness.READ;
        }
    }

    // Expressions are evaluated left-right, depth first.
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            if (state != VariableLiveness.MAYBE_LIVE) {
                return state;
            }
        }
    }
    return VariableLiveness.MAYBE_LIVE;
}