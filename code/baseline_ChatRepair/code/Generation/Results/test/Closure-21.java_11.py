public void visit(NodeTraversal t, Node n, Node parent) {
  // Handling unneeded special cases; return early if the node should not be processed.
  if (n.isEmpty() || n.isComma() || parent == null || n.isExprResult()) {
    return;
  }

  // This no-op statement allows JSDoc information to be attached.
  if (n.isQualifiedName() && n.getJSDocInfo() != null) {
    return;
  }

  // Determine if the expression results are utilized.
  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());

  if (parent.getType() == Token.COMMA && !isResultUsed && n == parent.getLastChild()) {
    for (Node ancestor : parent.getAncestors()) {
      int ancestorType = ancestor.getType();
      if (ancestorType == Token.COMMA) continue; // Dive deeper through commas
      if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK) return; // Stop at a statement or block level
      else break; // Only break if it's an expression result or block, which are handled in depth
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (!(parent.getType() == Token.FOR && parent.getChildCount() == 4 &&
          (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
      return;
    }
  }

  // Provide a warning if the node operation shows no side-effects but should
  if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }

    // Reporting the issue. Assumed USELESS_CODE_ERROR exists as a static identifier in some class.
    t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, "USELESS_CODE_ERROR", msg));
 
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
