private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    
    VariableLiveness currentResult = VariableLiveness.MAYBE_LIVE;
    
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            
            currentResult = VariableLiveness.KILL;
        } else {
            
            return VariableLiveness.READ;
        }
    }

    
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { 
            VariableLiveness childState = isVariableReadBeforeKill(child, variable);
            
            if (childState == VariableLiveness.READ) {
                return VariableLiveness.READ;
            }
            
            if (childState == VariableLiveness.KILL && currentResult != VariableLiveness.READ) {
                currentResult = VariableLiveness.KILL;
            }
        }
    }
    
    return currentResult; 