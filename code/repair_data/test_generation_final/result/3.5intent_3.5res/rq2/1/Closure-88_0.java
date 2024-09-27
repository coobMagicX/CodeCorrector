private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
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
    }
  }
  return VariableLiveness.MAYBE_LIVE;
}

private void tryRemoveDeadAssignments(NodeTraversal t, ControlFlowGraph<Node> cfg) {
  Iterable<DiGraphNode<Node, Branch>> nodes = cfg.getDirectedGraphNodes();

  for (DiGraphNode<Node, Branch> cfgNode : nodes) {
    FlowState<LiveVariableLattice> state =
        cfgNode.getAnnotation();
    Node n = cfgNode.getValue();
    if (n == null) {
      continue;
    }
    switch (n.getType()) {
      case Token.IF:
      case Token.WHILE:
      case Token.DO:
        tryRemoveAssignment(t, NodeUtil.getConditionExpression(n), state);
        continue;
      case Token.FOR:
        if (!NodeUtil.isForIn(n)) {
          tryRemoveAssignment(
              t, NodeUtil.getConditionExpression(n), state);
        }
        continue;
      case Token.SWITCH:
      case Token.CASE:
      case Token.RETURN:
        if (n.hasChildren()) {
          tryRemoveAssignment(t, n.getFirstChild(), state);
        }
        continue;
      // TODO(user): case Token.VAR: Remove var a=1;a=2;.....
    }

    tryRemoveAssignment(t, n, state);
  }
}