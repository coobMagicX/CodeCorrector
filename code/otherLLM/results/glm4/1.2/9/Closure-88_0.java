private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    // The variable is used as a NAME in the current node.
    if (n.getParent() != null) {
      // Check if this usage of 'variable' is on the LHS of an assignment
      Node parent = n.getParent();
      Node prevSibling = parent.getPrevSibling();

      while (prevSibling != null && prevSibling.getType() == Token.EQ) {
        // The variable is used as a NAME after an '=' operator which indicates
        // this usage is part of the LHS of an assignment.
        if (NodeUtil.isLhs(parent, n.getParent())) {
          // If the previous node is an assignment and it's not on the LHS,
          // then it's possible that the variable is read before being killed.
          return VariableLiveness.READ;
        }
        prevSibling = prevSibling.getPrevSibling();
        parent = prevSibling;
      }
    }

    // If we reach here, the variable is not used as the LHS of an assignment,
    // so it should be considered KILL if there is a kill-point.
    return NodeUtil.isKillPoint(n.getParent()) ? VariableLiveness.KILL : VariableLiveness.READ;
  }

  for (Node child = n.getFirstChild();
      child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
      VariableLiveness state = isVariableReadBeforeKill(child, variable);
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
  }

  // If the loop exits without returning any specific state, it's maybe live.
  return VariableLiveness.MAYBE_LIVE;
}