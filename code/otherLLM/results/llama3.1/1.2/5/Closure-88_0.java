private VariableLiveness isVariableReadBeforeKill(
    Node n, String variable) {
  if (NodeUtil.isName(n) && variable.equals(n.getString())) {
    // Consider left-hand side (LHS) evaluation order
    if (NodeUtil.isLhs(n, n.getParent())) {
      return VariableLiveness.KILL;
    } else {
      // Check child nodes more thoroughly for READ state
      NodeTraversal t = new NodeTraversal();
      visit(t, n, null);
      for (Node child : t.getChildren()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) { // Not a FUNCTION
          VariableLiveness state = isVariableReadBeforeKill(child, variable);
          if (state == VariableLiveness.READ) {
            return VariableLiveness.READ;
          }
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
  return VariableLiveness.MAYBE_LIVE;
}

public void visit(NodeTraversal t, Node n, Node parent) {
  // Initialize children traversal
  t.initTraversal(n);
}