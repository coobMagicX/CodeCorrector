public boolean apply(Node n) {
  if (n == null) {
    // When the node is null it means, we reached the implicit return
    // where the function returns (possibly without an return statement)
    return false;
  }

  // Any side effect in calls or constructors that are not analyzed to explicitly manage side effects
  if ((n.isCall() && NodeUtil.functionCallHasSideEffects(n)) ||
      (n.isNew() && NodeUtil.constructorCallHasSideEffects(n))) {
    return true;
  }

  // Recursively apply the function to child nodes, except when entering a new control flow graph node
  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }
  return false;
}
