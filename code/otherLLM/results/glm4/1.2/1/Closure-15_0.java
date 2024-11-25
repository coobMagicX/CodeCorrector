public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without an return statement)
  if (n == null) {
    return false;
  }

  // Check for delete-like operations as potential side effects that should not be inlined.
  if (NodeUtil.isDeleteOperation(n)) {
    return true; // Return true to indicate a potential side effect was detected.
  }

  // Existing checks for function and constructor calls with side effects
  if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
    return true;
  }
  if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
    return true;
  }

  // Traverse children nodes recursively to find any potential side effects.
  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }
  return false;
}