private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    
    VariableLiveness result = VariableLiveness.MAYBE_LIVE;

    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            
            
            result = VariableLiveness.KILL;
        } else {
            
            return VariableLiveness.READ;
        }
    }

    
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { 
            VariableLiveness state = isVariableReadBeforeKill(child, variable);

            
            if (state == VariableLiveness.READ) {
                return VariableLiveness.READ;
            }

            
            if