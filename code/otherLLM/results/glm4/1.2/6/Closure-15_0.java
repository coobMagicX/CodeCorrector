public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without a return statement)
  if (n == null) {
    return false;
  }

  // Check if the current node is a call to a function that has side effects
  if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
    return true;
  }

  // Check if the current node is a constructor call that has side effects
  if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
    return true;
  }

  // Check for delete operation as a potential side effect
  if (n.isDelete()) {
    return true;
  }

  // Traverse the children to check for side effects in recursive patterns
  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    // We need to ensure that we're only traversing into new control flow graph nodes
    if (!ControlFlowGraph.isEnteringNewCfgNode(c)) {
      if (apply(c)) {
        return true;
      }
    }
  }

  // No side effects detected in this node's subtree
  return false;
}