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

  // Handle delete statement separately in the control flow graph
  if (n.isDelete()) {
    ControlFlowGraph.isEnteringNewCfgNode(n);
    return false; // No side effects for delete statements
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }
  
  // Update the logic to include operations involving array iteration (`in` operator)
  if (n.isInOperator() || n.isArrayIteration()) {
    if (NodeUtil.functionCallHasSideEffects(n)) {
      return true; // Side effects for in operator or array iterations
    }
  }

  return false;
}