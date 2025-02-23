public void visit(NodeTraversal t, Node n, Node parent) {
  // Existing code...
  
  if (parent.getType() == Token.COMMA) {
    if (n == parent.getLastChild()) {
      isResultUsed = NodeUtil.isExpressionResultUsed(parent);
      if (!isResultUsed) {
        // Report the error as the result of the parent node (expression sequence) is not used
        t.getCompiler().report(t.makeError(parent, level, USELESS_CODE_ERROR,
            "The result of this comma expression is not being used."));
        return;
      }
    } else {
      return; // Return early since non-last children in a comma expression are never used.
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (!(parent.getType() == Token.FOR && parent.getChildCount() == 4 &&
          (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
      return;
    }
  }

  if ((isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
          "' operator is not being used.";
    }

    t.getCompiler().report(
        t.makeError(n, level, USELESS_CODE_ERROR, msg));
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
