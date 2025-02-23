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
      // Re-assess if the result is used by checking parent node in comma expression
      isResultUsed = NodeUtil.isExpressionResultUsed(parent);
    }
    if (!isResultUsed) {
      String msg = "The result of this comma expression is not being used.";
      t.getCompiler().report(t.makeError(n, level, USELESS_CODE_ERROR, msg));
      return;
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (!(parent.getType() == Token.FOR && parent.getChildCount() == 4 &&
         (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
      return;
    }
  }

  if (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
            "' operator is not being used.";
    }

    t.getCompiler().report(t.makeError(n, Diagnostic.GROUP_MISSING_USE, USELESS_CODE_ERROR, msg));
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
