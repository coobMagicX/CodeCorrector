private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    // Check if the node is an LHS and return accordingly.
    if (NodeUtil.isLhs(n, n.getParent())) {
      // The assignment happens before the RHS evaluation in normal left to right
      // evaluation order. We need to consider that a variable read after its
      // scope ends might be killed if not reassigned. Hence, we should return
      // VariableLiveness.MAYBE_LIVE here.
      return VariableLiveness.MAYBE_LIVE;
    } else {
      // If the node is not an LHS, it could potentially read a variable that
      // might get killed before its scope ends.
      return VariableLiveness.READ;
    }
  }

  // Expressions are evaluated left-right, depth first.
  for (Node child = n.getFirstChild();
      child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
      VariableLiveness state = isVariableReadBeforeKill(child, variable);
      // Return the state immediately if it's not MAYBE_LIVE because that indicates
      // the variable is either definitely read or killed.
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
  }
  // If all checks are passed and no definite kill has been found, return VariableLiveness.KILL.
  return VariableLiveness.KILL;
}