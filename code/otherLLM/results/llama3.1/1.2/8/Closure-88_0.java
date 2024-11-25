private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    if (NodeUtil.isLhs(n, n.getParent())) {
      // The expression to which the assignment is made is evaluated before
      // the RHS is evaluated (normal left to right evaluation) but the KILL
      // occurs after the RHS is evaluated.
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
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
      
      // Check if the child node is an assignment to the same variable
      if (NodeUtil.isName(child) && variable.equals(child.getString())) {
        // If it's an assignment, check if the variable was read before being assigned
        VariableLiveness childState = isVariableReadBeforeKill(child.getFirstChild(), variable);
        if (childState != VariableLiveness.MAYBE_LIVE) {
          return childState;
        }
      }
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}