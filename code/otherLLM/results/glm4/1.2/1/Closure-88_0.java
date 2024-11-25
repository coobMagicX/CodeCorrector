private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    // Check if the node is on the left-hand side of an assignment
    if (NodeUtil.isLhs(n, n.getParent())) {
      // If it's a function call and not inside an expression block like IF or SWITCH,
      // the variable might still be live because of closure.
      if (!ControlFlowGraph.isEnteringNewCfgNode(n)) {
        return VariableLiveness.MAYBE_LIVE;
      }
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
      if (state == VariableLiveness.KILL) {
        return VariableLiveness.KILL;
      }
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}