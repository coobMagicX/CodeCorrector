public void visit(NodeTraversal t, Node n, Node parent) {
  // VOID nodes appear with extra semicolons at the BLOCK level.
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
    // Check if the node is the last in a list or has a side-effect
    if (n == parent.getLastChild() || !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
      return;
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (!(parent.getType() == Token.FOR && parent.getChildCount() == 4 && 
          (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
      return;
    }
  }

  if (!isResultUsed && (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }

    t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, msg));
    
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
