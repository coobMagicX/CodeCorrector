public void visit(NodeTraversal t, Node n, Node parent) {
    // VOID nodes appear when there are extra semicolons at the BLOCK level.
    // I've been unable to think of any cases where this indicates a bug,
    // and apparently some people like keeping these semicolons around,
    // so we'll allow it.
    if (n.isEmpty() ||
        n.isComma()) {
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
        // Repair strategy: Check for the presence of side effects before reporting an error.
        if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler()) && !isResultUsed) {
            String msg = "This code lacks side-effects. Is there a bug?";
            if (n.isString()) {
                msg = "Is there a missing '+' on the previous line?";
            } else if (isSimpleOp) {
                msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
                      "' operator is not being used.";
            }

            t.getCompiler().report(
                t.makeError(n, level, USELESS_CODE_ERROR, msg));
        }
    } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
        // Repair strategy: Ensure that expressions are properly evaluated
        // before reporting an error.
        if (!NodeUtil.isExpressionResultUsed(n)) {
            String msg = "The result of this expression is not used.";
            t.getCompiler().report(
                t.makeError(n, level, USELESS_CODE_ERROR, msg));
        }
    }

    // Repair strategy: Continue to process child nodes as necessary.
    if (!NodeUtil.isStatement(n) && n.hasChildren()) {
      for (Node child : n.children()) {
          visit(t, child, n);
      }
    }
}