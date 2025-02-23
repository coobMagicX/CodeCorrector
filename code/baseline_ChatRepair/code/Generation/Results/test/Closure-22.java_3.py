public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() ||
      n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  // Handle specific case for unterminated expressions within assignments or other structures
  if (parent.isAssign() && n.isComma()) {
    Node lastChild = n.getLastChild();
    if (!NodeUtil.mayHaveSideEffects(lastChild, t.getCompiler())) {
      String message = "The result of the expression is not used.";
      t.getCompiler().report(JSError.make(n, USELESS_CODE_ERROR, message));
      return;
    }
  }

  // Existing checks...
  if (parent.getType() == Token.COMMA) {
    Node gramps = parent.getParent();
    if (gramps.isCall() && parent == gramps.getFirstChild()) {
      if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
        return;
      }
    }
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean hasSideEffects = NodeUtil.mayHaveSideEffects(n, t.getCompiler());

  // Further checks...
  if (!isResultUsed && !hasSideEffects) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (NodeUtil.isSimpleOperator(n)) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }
    t.getCompiler().report(t.makeError(n, DiagnosticGroups.CHECK_SIDE_EFFECTS, msg));
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }  
  }
}
