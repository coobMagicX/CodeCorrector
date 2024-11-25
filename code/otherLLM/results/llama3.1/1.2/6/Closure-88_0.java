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
      
      // Check for nested function calls or expressions with multiple assignment statements
      if ((child.getType() == Token.FUNCTION || child.getType() == Token.METHOD)
          && NodeUtil.isAssignmentStatement(child)) {
        // If the variable is assigned a new value in a nested function call,
        // it may have been read before being killed.
        return VariableLiveness.MAYBE_LIVE;
      }
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}