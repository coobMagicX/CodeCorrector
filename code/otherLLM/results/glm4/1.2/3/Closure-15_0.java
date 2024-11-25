public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without an return statement)
  if (n == null) {
    return false;
  }

  // Check for deletion operations which can have side effects
  if (n.isDelete() && NodeUtil.functionCallHasSideEffects(n)) {
    // We need to check right of delete operation for side effects
    if (checkRightOf(n, n.getParent(), cur -> NodeUtil.functionCallHasSideEffects(cur))) {
      return true;
    }
  }

  // TODO(user): We only care about calls to functions that
  // passes one of the dependent variable to a non-side-effect free
  // function.
  if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
    return true;
  }

  if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
    return true;
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    // Ensure we only check children that can have side effects
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }
  return false;
}