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
    } else if (child.getString().equals(variable)) {
      VariableLiveness childState = isVariableReadBeforeKill(child, variable);
      if (childState == VariableLiveness.READ) {
        return VariableLiveness.READ;
      }
    }
  }
  // Check if the variable is read in a parent node
  Node parentNode = n.getParent();
  while (parentNode != null) {
    VariableLiveness state = isVariableReadBeforeKill(parentNode, variable);
    if (state == VariableLiveness.READ) {
      return VariableLiveness.READ;
    }
    parentNode = parentNode.getParent();
  }

  return VariableLiveness.MAYBE_LIVE;
}