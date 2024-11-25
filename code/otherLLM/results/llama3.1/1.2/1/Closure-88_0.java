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
    if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { 
      // Check if the variable is not read before kill in any of the child nodes
      VariableLiveness state = isVariableReadBeforeKill(child, variable);
      if (state == VariableLiveness.READ) { // If the variable is read in a child node, return READ
        return VariableLiveness.READ;
      } else if (state != VariableLiveness.MAYBE_LIVE) {
        return state; // If the variable is killed or not alive in any of the child nodes, return that state
      }
    }
  }
  // If the variable is not read before kill in the current node and its children, return MAYBE_LIVE
  return VariableLiveness.MAYBE_LIVE;
}