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

  if (n.getType() == Token.BLOCK) {
    // If the node is a block, check if the variable is killed or read in any of its children.
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
      VariableLiveness state = isVariableReadBeforeKill(child, variable);
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
  }
  
  // If the node is a control flow statement, check if the variable is killed or read in any of its children.
  if (NodeUtil.isControlStructure(n)) {
    Node condition = NodeUtil.getConditionExpression(n);
    if (condition != null) {
      VariableLiveness state = isVariableReadBeforeKill(condition, variable);
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
    
    Node body = NodeUtil.getControlStructureBody(n);
    if (body != null) {
      VariableLiveness state = isVariableReadBeforeKill(body, variable);
      if (state != VariableLiveness.MAYBE_LIVE) {
        return state;
      }
    }
  }
  
  // If the node has children, check if the variable is killed or read in any of its children.
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    VariableLiveness state = isVariableReadBeforeKill(child, variable);
    if (state != VariableLiveness.MAYBE_LIVE) {
      return state;
    }
  }

  return VariableLiveness.MAYBE_LIVE;
}