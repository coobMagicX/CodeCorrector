public boolean apply(Node n) {
  // When the node is null, it means we reached the implicit return
  // where the function returns (possibly without a return statement)
  if (n == null) {
    return false;
  }

  // Check for calls to functions and new constructor calls that have side effects
  if ((n.isCall() && NodeUtil.functionCallHasSideEffects(n)) ||
      (n.isNew() && NodeUtil.constructorCallHasSideEffects(n))) {
    return true;
  }

  // Additional checks for potentially problematic operations such as 'delete'
  if (n.isDelProp()) {
    return true;
  }

  // Iterate over children nodes to detect side effects recursively
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child) && apply(child)) {
      return true;
    }
  }
  return false;
}
