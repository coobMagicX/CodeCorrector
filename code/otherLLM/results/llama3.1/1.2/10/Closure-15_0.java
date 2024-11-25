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

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    // Check if the node is a control flow graph node and its parent is not
    // entering a new CFG node. If so, recursively call apply on the child.
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }

    // Also check if the node is an expression involving the 'in' operator
    // and its parent is not entering a new CFG node. If so, evaluate the 
    // expression using visit method.
    if (c.isExprIn() && !ControlFlowGraph.isEnteringNewCfgNode(c.getParent())) {
      visit(null, c, null);  // This line was added to fix the bug
      return true;
    }
  }

  return false;
}