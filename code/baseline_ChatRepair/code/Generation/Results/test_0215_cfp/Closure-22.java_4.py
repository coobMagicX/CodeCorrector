public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  // Check for unused expression results in non-top-levels of comma expressions
  if (n.isExprResult() && !NodeUtil.isExpressionResultUsed(n) &&
      NodeUtil.isStatement(n) && parent.isBlock()) {
    t.getCompiler().report(
        t.makeError(n, CheckLevel.WARNING, "This code lacks side-effects. Is there a bug?"));
    return;
  }

  // Regular checks for side-effects
  if (!NodeUtil.isExpressionResultUsed(n) && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (NodeUtil.isSimpleOperator(n)) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }
    t.getCompiler().report(
        t.makeError(n, CheckLevel.WARNING, msg));
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
