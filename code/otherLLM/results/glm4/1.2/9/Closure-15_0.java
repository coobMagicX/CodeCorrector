public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without a return statement)
  if (n == null) {
    return false;
  }

  // Check for side effects in expressions resulting from an 'in' operator
  if (n.isExpression() && NodeUtil.expressionHasSideEffects(n)) {
    boolean hasDependentVariableAccess = checkRightOf(
      n, n, c -> c.isVariableAccess() && NodeUtil.hasDependentVariables(c));
    if (hasDependentVariableAccess) {
      return true;
    }
  }

  // Check for side effects in function calls
  if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
    return true;
  }

  // Check for side effects in constructor calls
  if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
    return true;
  }

  // Process the children nodes recursively to check for control flow changes
  boolean hasSideEffectChild = false;
  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true; // Early return if a side effect is found in a child
    }
    hasSideEffectChild |= NodeUtil.hasSideEffects(c);
  }

  // Return true if any of the children had side effects, otherwise false
  return hasSideEffectChild;
}

// Method for checking right-hand expressions for dependent variable access
private static boolean checkRightOf(
  Node n, Node expressionRoot, Predicate<Node> predicate) {
  for (Node p = n; p != expressionRoot; p = p.getParent()) {
    for (Node cur = p.getNext(); cur != null; cur = cur.getNext()) {
      if (predicate.apply(cur)) {
        return true;
      }
    }
  }
  return false;
}