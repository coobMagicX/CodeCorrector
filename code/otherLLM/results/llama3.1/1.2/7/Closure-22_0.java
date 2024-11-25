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
  if (parent.getType() == Token.COMMA) {
    Node gramps = parent.getParent();
    if (gramps.isCall() && parent == gramps.getFirstChild()) {
      if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
        // Allow eval statements, even if they have side effects
        return;
      }
    }

    // Check if the node is a simple operator in an implicit context
    if (NodeUtil.isSimpleOperatorType(n.getType()) && NodeUtil.isImplicitContext(n)) {
      return;
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() ||
         n == parent.getFirstChild().getNext().getNext())) {
      // Allow for loops with side effects
    } else {
      return;
    }
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  if (!isResultUsed) {
    // Check if the node has side effects or is a simple operator in an explicit context
    boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
    if (NodeUtil.mayHaveSideEffects(n, t.getCompiler()) || (isSimpleOp && !NodeUtil.isImplicitContext(n))) {
      // Check if the node has side effects or is a simple operator in an explicit context
      String msg = "This code lacks side-effects. Is there a bug?";
      if (n.isString()) {
        msg = "Is there a missing '+' on the previous line?";
      } else if (isSimpleOp) {
        msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
            "' operator is not being used.";
      }

      t.getCompiler().report(
          t.makeError(n, level, USELESS_CODE_ERROR, msg));
      // TODO(johnlenz): determine if it is necessary to
      // try to protect side-effect free statements as well.
      if (!NodeUtil.isStatement(n)) {
        problemNodes.add(n);
      }
    } else {
      // If the node has no side effects or is a simple operator in an implicit context, allow it
      return;
    }
  }
}