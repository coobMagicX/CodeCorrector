private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            
            return checkReadBeforeKillInChildren(n, variable) ? VariableLiveness.READ : VariableLiveness.KILL;
        } else {
            return VariableLiveness.READ;
        }
    }

    
    return checkReadBeforeKillInChildren(n, variable) ? VariableLiveness.READ : VariableLiveness.MAYBE_LIVE;
}

private boolean checkReadBeforeKillInChildren(Node n, String variable) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { 
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            if (state == VariableLiveness.READ) {
                return true;
            } else if (state == VariableLiveness.KILL) {
                
                boolean readLater = checkReadBeforeKillInChildren(child.getNext(), variable);
                if (readLater) return true;
            }
        }
    }
    return false;
}
