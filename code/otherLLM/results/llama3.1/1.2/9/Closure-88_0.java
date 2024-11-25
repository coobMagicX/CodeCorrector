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
      if (state == VariableLiveness.READ || state == VariableLiveness.KILL) {
        return state;
      }
    }
  }

  // If we have not returned by now, it means the variable might be read or killed later in the AST
  // So, we need to check if the variable is still live within its parent's expression
  boolean isVariableStillLive = false;
  Node exprRoot = n.getParent();
  while (exprRoot != null && !isVariableStillLive) {
    isVariableStillLive = isVariableStillLiveWithinExpression(n, exprRoot, variable);
    exprRoot = exprRoot.getParent();
  }

  if (!isVariableStillLive) {
    return VariableLiveness.READ;
  } else {
    return VariableLiveness.MAYBE_LIVE;
  }
}