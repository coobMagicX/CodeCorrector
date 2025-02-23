public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  if (parent.getType() == Token.COMMA) {
    Node grandparent = parent.getParent();
    if (grandparent.isCall() && parent == grandparent.getFirstChild()) {
      if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
        return;
      }
    }
    // Check middle elements in a comma expression for non-side effects
    if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler()) && n != parent.getLastChild()) {
      String msg = "Non-side-effect intermediate value in comma expression";
      t.getCompiler().report(
          t.makeError(n, DiagnosticLevel.WARNING, USELESS_CODE_ERROR, msg));
      return;
    }
  }

  if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    return;
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());

  if (!isResultUsed && (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }

    t.getCompiler().report(
        t.makeError(n, DiagnosticLevel.WARNING, USELESS_CODE_ERROR, msg));

    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
