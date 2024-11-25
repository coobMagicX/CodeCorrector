public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without a return statement)
  if (n == null) {
    return false;
  }

  // Analyze whether this call has side effects or not.
  // NodeUtil should be checking for dependent variables being passed to functions/constructors with side effects.
  boolean hasSideEffects = false;

  if (n.isCall()) {
    hasSideEffects = NodeUtil.functionCallHasSideEffects(n);
  } else if (n.isNew()) {
    hasSideEffects = NodeUtil.constructorCallHasSideEffects(n);
  }

  // If the call or constructor invocation has side effects, return true.
  if (hasSideEffects) {
    return true;
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c)) {
      if (apply(c)) {
        // If any child node has side effects, propagate the result to this node.
        return true;
      }
    }
  }
  // If no side effects are found in any call or constructor invocation in the subtree,
  // then there are no side effects for this node.
  return false;
}