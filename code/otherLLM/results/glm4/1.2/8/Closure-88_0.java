private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    Node parent = n.getParent();
    if (NodeUtil.isLhs(n, parent)) {
      // The expression to which the assignment is made is evaluated before
      // the RHS is evaluated (normal left-to-right evaluation), but the KILL
      // occurs after the RHS is evaluated.
      return VariableLiveness.KILL;
    } else {
      // If the variable is read from the LHS of an assignment, it's killed there,
      // otherwise we should check if it's live in any other expression context.
      Node lhs = parent.getFirstChild();
      while (lhs != null && !variable.equals(lhs.getString())) {
        lhs = lhs.getNextSibling();
      }
      if (lhs == null || NodeUtil.isAssign(parent)) {
        return VariableLiveness.KILL;
      } else {
        return VariableLiveness.READ;
      }
    }
  }

  // Expressions are evaluated left-right, depth first.
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
      VariableLiveness state = isVariableReadBeforeKill(child, variable);
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}