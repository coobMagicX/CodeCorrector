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
    if (n != parent.getLastChild()) {
      // Check middle children in a comma expression
      if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
        reportUselessCode(n, t, "The result of this expression is not being used.");
      }
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    return; // The parent itself controls the flow; ignore current node
  }

  if (!isResultUsed && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    reportUselessCode(n, t, "This code lacks side-effects. Is there a bug?");
  }
}

/**
* Helper method to report the useless code.
* @param n The node causing the report.
* @param t The traversal context.
* @param message The message for the report.
*/
private void reportUselessCode(Node n, NodeTraversal t, String message) {
  t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, message));
  if (!NodeUtil.isStatement(n)) {
    problemNodes.add(n);
  }
}
