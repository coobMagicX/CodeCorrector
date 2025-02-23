public void visit(NodeTraversal t, Node n, Node parent) {
  ...
  
  if (n.isExprResult()) {
    Node expr = n.getFirstChild();
    checkForUnusedExpressionResults(t, expr);
    return;
  }

  ...
}

private void checkForUnusedExpressionResults(NodeTraversal t, Node expr) {
  if (expr.isComma()) {
    Node child = expr.getFirstChild();
    while (child != null) {
      checkForUnusedExpressionResults(t, child);
      child = child.getNext();
    }
  } else {
    boolean isResultUsed = NodeUtil.isExpressionResultUsed(expr);
    boolean isSimpleOp = NodeUtil.isSimpleOperatorType(expr.getType());
    if (!isResultUsed && (isSimpleOp || !NodeUtil.mayHaveSideEffects(expr, t.getCompiler()))) {
      // Report based on the type of operation or expression; similar to 'report' snippet you have in your code.
      String msg = determineAppropriateMessage(expr);
      t.getCompiler().report(t.makeError(expr, level, USELESS_CODE_ERROR, msg));
    }
  }
}

private String determineAppropriateMessage(Node n) {
  if (n.isQualifiedName()) {
    return "This qualified name's result is unused. Possible oversight.";
  } else if (n.isSimpleOp()) {
    return "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
  } else {
    return "This code lacks side-effects. Is there a bug?";
  }
}
...
