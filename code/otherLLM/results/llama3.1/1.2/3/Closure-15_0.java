public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without an return statement)
  if (n == null) {
    return false;
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

  // Specialized handling for "in" operator
  if (n.isIn()) {
    // Check if the left-hand side of the "in" operator has any side effects
    if (NodeUtil.functionCallHasSideEffects(n.getLeft())) {
      return true;
    }
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }

  // Check right-hand side of "in" operator
  if (n.isIn() && checkRightOf(n.getRight(), n, NodeUtil.functionCallHasSideEffects)) {
    return true;
  }

  return false;
}