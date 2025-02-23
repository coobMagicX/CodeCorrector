private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            
            
            
            return VariableLiveness.KILL;
        } else {
            
            return VariableLiveness.READ;
        }
    }

    
    VariableLiveness consolidatedState = VariableLiveness.MAYBE_LIVE;
    
    
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) {
            
            VariableLiveness childState = isVariableReadBeforeKill(child, variable);
            
            