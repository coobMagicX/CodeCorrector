private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        // Check for assignment and logical expressions
        if (NodeUtil.isAssignment(n)) {
            Node lhs = NodeUtil.getLhs(n);
            if (variable.equals(lhs.getString()) && !ControlFlowGraph.isEnteringNewCfgNode(n.getNext())) {
                return VariableLiveness.KILL; // KILL after RHS is evaluated
            }
        } else if (NodeUtil.isLogicalExpression(n) && variable.equals(NodeUtil.getOpnd1(n).getString())) {
            // Logical expressions have left-to-right evaluation
            Node rightOpnd = NodeUtil.getOpnd2(n);
            VariableLiveness stateRightOpnd = isVariableReadBeforeKill(rightOpnd, variable);
            if (stateRightOpnd == VariableLiveness.KILL) {
                return VariableLiveness.READ; // READ before the logical operation
            }
        }

        if (NodeUtil.isLhs(n, n.getParent())) {
            return VariableLiveness.KILL;
        } else {
            return VariableLiveness.READ;
        }
    }

    // Expressions are evaluated left-right, depth first.
    for (Node child = n.getFirstChild();
         child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            if (state == VariableLiveness.KILL) {
                return VariableLiveness.KILL; // Immediate KILL if found in any child
            }
        }
    }

    // If not killed or read before kill in this path, it may be live elsewhere
    return VariableLiveness.MAYBE_LIVE;
}