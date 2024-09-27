private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    if (NodeUtil.isLhs(n, n.getParent())) {
      // Check if the variable is reassigned within the same expression
      if (isVariableReassigned(n)) {
        // The variable is killed and read within the same expression
        return VariableLiveness.KILL_AND_READ;
      } else {
        // The expression to which the assignment is made is evaluated before
        // the RHS is evaluated (normal left to right evaluation) but the KILL
        // occurs after the RHS is evaluated.
        return VariableLiveness.KILL;
      }
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
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}

private boolean isVariableReassigned(Node n) {
  if (NodeUtil.isAssignmentOp(n.getParent())) {
    Node lhs = n.getParent().getFirstChild();
    if (lhs != null && lhs == n) {
      return true;
    }
  }
  return false;
}