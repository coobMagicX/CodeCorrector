private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  // Check if the current node is a variable and matches the given variable name.
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    // If the variable is on the left-hand side of an assignment, it is read before being killed.
    if (NodeUtil.isLhs(n, n.getParent())) {
      return VariableLiveness.READ;
    } 
    // If the variable is not on the left-hand side of an assignment, it may be reassigned or have a complex scope.
    else {
      // Recursively check child nodes for potential reads before kill.
      for (Node child = n.getFirstChild();
          child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
          VariableLiveness state = isVariableReadBeforeKill(child, variable);
          // If a read before kill is found in any child node, return that state.
          if (state != VariableLiveness.MAYBE_LIVE) {
            return state;
          }
        }
      }
    }
  }

  // Expressions are evaluated left-right, depth first.
  for (Node child = n.getFirstChild();
      child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
      VariableLiveness state = isVariableReadBeforeKill(child, variable);
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}