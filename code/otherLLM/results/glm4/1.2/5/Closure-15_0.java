public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without a return statement)
  if (n == null) {
    return false;
  }

  // TODO(user): Check for calls to functions that pass dependent variables
  // to non-side-effect free functions.
  if (n.isCall()) {
    NodeUtil.FunctionCallInfo callInfo = NodeUtil.getFunctionCallInfo(n);
    if (callInfo != null && NodeUtil.functionCallHasSideEffects(callInfo)) {
      return true;
    }
  }

  if (n.isNew()) {
    NodeUtil.ConstructorCallInfo callInfo = NodeUtil.getConstructorCallInfo(n);
    if (callInfo != null && NodeUtil.constructorCallHasSideEffects(callInfo)) {
      return true;
    }
  }

  // Check for side effects in expressions or statements that are not control flow instructions
  if (n.isExpression() || n.isStatement()) {
    boolean hasSideEffect = false;
    if (n.isExpression()) {
      Node expressionRoot = NodeUtil.getExpressionRoot(n);
      hasSideEffect = checkRightOf(n, expressionRoot, node -> NodeUtil.hasSideEffects(node));
    } else if (n.isStatement()) {
      hasSideEffect = NodeUtil.hasSideEffects(n);
    }
    if (hasSideEffect) {
      return true;
    }
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    // Skip nodes that do not enter a new control flow graph
    if (!ControlFlowGraph.isEnteringNewCfgNode(c)) {
      if (apply(c)) {
        return true;
      }
    }
  }

  return false;
}