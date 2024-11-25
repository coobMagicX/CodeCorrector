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

  boolean childResult = false;
  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    // Check if we're entering a new CFG node, to avoid infinite loops
    if (!ControlFlowGraph.isEnteringNewCfgNode(c)) {
      childResult |= apply(c);
    }
  }

  return checkRightOf(n, n.getParent(), SIDE_EFFECT_PREDICATE) || childResult;
}

private static boolean checkRightOf(
    Node n, Node expressionRoot, Predicate<Node> predicate) {
  // Check if there's a right-hand side to the expression
  if (expressionRoot.getKind() == Kind.POSTFIX_OPERATOR) {
    return checkLeftOf(n, expressionRoot.getChild(1), predicate);
  }
  return false;
}

private static boolean checkLeftOf(
    Node n, Node expressionRoot, Predicate<Node> predicate) {
  // Check if there's a left-hand side to the expression
  if (expressionRoot.getKind() == Kind.PREFIX_OPERATOR) {
    return predicate.apply(expressionRoot.getChild(0));
  }
  return false;
}