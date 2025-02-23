public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  if (n.isExprResult()) {
    return;
  }

  if (n.isQualifiedName() && n.getJSDocInfo() != null) {
    return;
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
  if (parent.getType() == Token.COMMA) {
    if (n == parent.getLastChild()) {
      boolean hasSideEffectsOrIsUsed = NodeUtil.mayHaveSideEffects(n, t.getCompiler()) || isResultUsed;
      if (!hasSideEffectsOrIsUsed) {
        reportUselessCode(t, n, "Code does not have any effect and result of the expression is not used.");
      }
      return; // Only analyze the last child in comma without side effects.
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    return; // We skip any reporting as these are managed at their parent level.
  }

  if (!isSimpleOp && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    reportUselessCode(t, n, "This code lacks side-effects. Is there a bug?");
  }
}

private void reportUselessCode(NodeTraversal t, Node n, String msg) {
  t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, msg));
  if (!NodeUtil.isStatement(n)) {
    problemNodes.add(n);
  }
}
