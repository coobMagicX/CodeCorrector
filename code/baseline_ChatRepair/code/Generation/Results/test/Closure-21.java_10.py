public void visit(NodeTraversal t, Node n, Node parent) {
  // Handling unneeded special cases; return early.
  if (n.isEmpty() || n.isComma() || parent == null || n.isExprResult()) {
    return;
  }

  // This no-op statement was there so that JSDoc information could be attached.
  if (n.isQualifiedName() && n.getJSDocInfo() != null) {
    return;
  }

  // Check if the node may have side effects. If it's an expression result used, it might have side effects.
  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  if (parent.getType() == Token.COMMA && !isResultUsed && n == parent.getLastChild()) {
    for (Node ancestor : parent.getAncestors()) {
      int ancestorType = ancestor.getType();
      if (ancestorType == Token.COMMA) continue;
      if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK) return;
      else break;
    }
  }

  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
  if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (!(parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
      return;
    }
  }

  // Check side-effects for expression statement.
  if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
          "' operator is not being used.";
    }

    t.getCompiler().report(
        t.makeError(n, JSError.make.LEVEL_WARNING, "USELESS_CODE_ERROR", msg));
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
