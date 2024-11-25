public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without an return statement)
  if (n == null) {
    return false;
  }

  // Check if the call has side effects including 'delete'
  if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
    return true;
  }

  // Check if the constructor call has side effects
  if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
    return true;
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    // Ensure we are not entering a new control flow graph node
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }

  // No side effects detected, so check for inlined assignments
  if (n.isAssign()) {
    Node lhs = n.getFirstChild();
    Node rhs = n.getSecondChild();

    // Check if the right-hand side is a function or constructor call that has side effects
    if (rhs.isCall() && NodeUtil.functionCallHasSideEffects(rhs)) {
      return true;
    }

    // Check for 'delete' operation on the left-hand side, which should have a side effect
    if (lhs.isDelete()) {
      return true;
    }
  }

  return false;
}