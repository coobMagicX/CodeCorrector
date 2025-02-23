public void visit(NodeTraversal t, Node n, Node parent) {
  // Skip processing for empty or comma nodes. Early return.
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  // Skip if no parent (root node).
  if (parent == null) {
    return;
  }

  // Skip if expression result or qualified name with JsDoc; considered intentionally used.
  if (n.isExprResult() || (n.isQualifiedName() && n.getJSDocInfo() != null)) {
    return;
  }

  // Gather context for decision.
  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean hasSideEffects = NodeUtil.mayHaveSideEffects(n, t.getCompiler());

  // Check within COMMA expressions.
  if (parent.getType() == Token.COMMA) {
    if (n != parent.getLastChild() && !hasSideEffects) {
      reportUselessCode(t, n, "Expression result not used.");
    }
  }

  // Check if node is a condition in an 'if' without side effects.
  if (parent.isIf() && !isResultUsed && !hasSideEffects) {
     reportUselessCode(t, n, "Expression in 'if' condition lacks side-effects and its result is unused.");
  }

  // Other situations where result is unused and has no side effects, warn for general useless code.
  if (!isResultUsed && !hasSideEffects) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    }
    reportUselessCode(t, n, msg);
  }
}

private void reportUselessCode(NodeTraversal t, Node n, String message) {
  t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, message));
  // Optionally collect problem nodes if needed for further processing or analysis.
  if (!NodeUtil.isStatement(n)) {
    problemNodes.add(n);
  }
}
