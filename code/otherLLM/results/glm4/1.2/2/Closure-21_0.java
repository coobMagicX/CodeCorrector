public void visit(NodeTraversal t, Node n, Node parent) {
  // VOID nodes appear when there are extra semicolons at the BLOCK level.
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  // Do not try to remove a block or an expr result. We already handle
  // these cases when we visit the child, and the peephole passes will
  // fix up the tree in more clever ways when these are removed.
  if (n.isExprResult()) {
    return;
  }

  // This no-op statement was there so that JSDoc information could
  // be attached to the name. This check should not complain about it.
  if (n.isQualifiedName() && n.getJSDocInfo() != null) {
    return;
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
  if (parent.getType() == Token.COMMA) {
    if (!isResultUsed) { // Check result use only when necessary
      for (Node an : parent.getAncestors()) {
        int ancestorType = an.getType();
        if (ancestorType == Token.COMMA || ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK) continue;
        else break;
      }
    } else {
      return; // If result is used, do not report useless code
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler())) { // Correct condition for checking side effects
      return;
    }
  }

  boolean hasNoSideEffects = isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler());
  if (hasNoSideEffects) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
            "' operator is not being used.";
    }

    t.getCompiler().report(
        t.makeError(n, 0, USELESS_CODE_ERROR, msg)); // Assuming 'level' is not required, using 0 as a default level
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}