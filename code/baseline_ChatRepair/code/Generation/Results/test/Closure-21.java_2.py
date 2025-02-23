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
    if (!isResultUsed) {
      if (n != parent.getLastChild()) {
        return; // Only complain about the last item in a comma expression not being used.
      }
      reportUselessCodeError(t, n, "The result of this comma expression is not being used.");
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (!(parent.getType() == Token.FOR && parent.getChildCount() == 4 &&
         (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
      return;
    }
  }

  if ((!isSimpleOp && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) || (isSimpleOp && !isResultUsed)) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
            "' operator is not being used.";
    }
    reportUselessCodeError(t, n, msg);
  }
}

private void reportUselessCodeError(NodeTraversal t, Node n, String msg) {
  t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, "USELESS_CODE", msg));
}
