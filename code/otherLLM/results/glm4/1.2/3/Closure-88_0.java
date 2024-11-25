private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    // Check for the base case where the current node represents the variable itself.
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            // The expression to which the assignment is made is evaluated before
            // the RHS is evaluated (normal left to right evaluation), and the KILL occurs after the RHS is evaluated.
            return VariableLiveness.KILL;
        } else {
            return VariableLiveness.READ;
        }
    }

    // Expressions are evaluated left-right, depth first. The state of a variable should be considered KILL
    // if any child node indicates that the variable has been killed before being read in this expression.
    boolean killBeforeRead = false;

    for (Node child = n.getFirstChild();
         child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            if (state == VariableLiveness.KILL) {
                killBeforeRead = true;
                break; // The variable is killed before being read in any child expression.
            }
        }
    }

    // If the variable is killed before being read, we return KILL, otherwise READ or MAYBE_LIVE as per the children's states.
    if (killBeforeRead) {
        return VariableLiveness.KILL;
    } else {
        for (Node child = n.getFirstChild();
             child != null; child = child.getNext()) {
            if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
                VariableLiveness state = isVariableReadBeforeKill(child, variable);
                if (state == VariableLiveness.READ) {
                    return VariableLiveness.READ;
                }
            }
        }
        // If no child indicates that the variable is read and no kill has occurred before,
        // we return MAYBE_LIVE to indicate uncertainty about the variable's liveness.
        return VariableLiveness.MAYBE_LIVE;
    }
}