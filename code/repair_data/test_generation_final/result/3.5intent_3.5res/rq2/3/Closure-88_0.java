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
  
  // Check if the current node is a compound assignment
  if (NodeUtil.isCompoundAssignmentOp(n)) {
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    VariableLiveness state = isVariableReadBeforeKill(lhs, variable);
    if (state == VariableLiveness.READ) {
      return VariableLiveness.READ;
    } else {
      return isVariableReadBeforeKill(rhs, variable);
    }
  }

  // Expressions are evaluated left-right, depth first.
  for (Node child = n.getFirstChild();
      child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
      VariableLiveness state = isVariableReadBeforeKill(child, variable);
      if (state == VariableLiveness.KILL) {
        return VariableLiveness.KILL;
      } else if (state == VariableLiveness.READ) {
        return VariableLiveness.READ;
      }
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}