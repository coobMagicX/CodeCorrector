private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    // Check for assignment to the variable and determine liveness
    if (NodeUtil.isAssignOp(n)) {
      Node lhs = NodeUtil.getLhs(n);
      if (lhs != null && variable.equals(lhs.getString())) {
        return VariableLiveness.KILL;
      }
    } else if (NodeUtil.isLhs(n, n.getParent())) {
      // The expression to which the assignment is made is evaluated before
      // the RHS is evaluated (normal left to right evaluation) but the KILL
      // occurs after the RHS is evaluated.
      return VariableLiveness.KILL;
    } else if (NodeUtil.isReadOp(n)) {
      // If it's a read operation, we consider the variable live at this point
      return VariableLiveness.LIVE;
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
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}