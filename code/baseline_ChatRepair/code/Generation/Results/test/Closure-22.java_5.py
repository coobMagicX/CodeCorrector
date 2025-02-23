public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  if (parent.getType() == Token.COMMA) {
    Node gramps = parent.getParent();
    if (gramps.isCall() && parent == gramps.getFirstChild()) {
      if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
        return;
      }
    }

    // Specific handling for sequence operations
    if (n.isNumber() || n.isString()) {
      Node next = n.getNext();
      if (next != null && (next.isNumber() || next.isString() || next.isSimpleOp())) {
        String msg = "Intermediate values in expressions (like '(foo(), 6, 7)' are unused";
        t.getCompiler().report(t.makeError(n, DiagnosticLevel.WARNING, USELESS_CODE_ERROR, msg));
        return;
      }
    }
  }

  if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK &&
      !(parent.getType() == Token.FOR && parent.getChildCount() == 4 &&
      (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
    return;
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
  
  if (!isResultUsed && (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
    if (n.isQualifiedName() && n.getJSDocInfo() != null) {
      return;
    } else if (n.isExprResult()) {
      return;
    }
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }

    t.getCompiler().report(t.makeError(n, DiagnosticLevel.WARNING, USELESS_CODE_ERROR, msg));
  }
}
