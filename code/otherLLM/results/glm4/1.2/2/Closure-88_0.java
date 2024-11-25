private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    // Check if the node is on the left-hand side of an assignment.
    if (NodeUtil.isLhs(n, n.getParent())) {
      // For an LHS, it's a READ because we're looking at the variable being written to.
      return VariableLiveness.READ;
    } else {
      // If not on LHS, then check if there is a KILL before a READ
      for (Node sibling = n.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
        if (ControlFlowGraph.isEnteringNewCfgNode(sibling)) { // It's a FUNCTION call
          return VariableLiveness.KILL;
        }
        if (NodeUtil.isName(sibling) && variable.equals(sibling.getString())) {
          // If we find the variable again, it means it's READ after writing or in another function scope.
          return VariableLiveness.READ;
        }
      }
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

  // If we've reached here without finding any READ or KILL before, it's MAYBE_LIVE.
  return VariableLiveness.MAYBE_LIVE;
}