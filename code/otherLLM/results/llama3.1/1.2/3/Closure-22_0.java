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
        return;
      }
      // Tighten the condition for checking useless code
      if (!NodeUtil.mayHaveSideEffects(parent, t.getCompiler()) ||
          !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
        if (n.isQualifiedName() && n.getJSDocInfo() != null) {
          return;
        } else if (n.isExprResult()) {
          return;
        }
        String msg = "This code lacks side-effects. Is there a bug?";
        if (n.isString()) {
          msg = "Is there a missing '+' on the previous line?";
        }

        t.getCompiler().report(
            t.makeError(n, level, USELESS_CODE_ERROR, msg));
        // TODO(johnlenz): determine if it is necessary to
        // try to protect side-effect free statements as well.
        if (!NodeUtil.isStatement(n)) {
          problemNodes.add(n);
        }
      }
    } else if (parent.getType() == Token.EXPR_RESULT) {
      // Handle comma expressions correctly
      Node lastChild = parent.getLastChild();
      while (lastChild != null && lastChild.isComma()) {
        lastChild = lastChild.getNext();
      }
      if (lastChild != n || !NodeUtil.mayHaveSideEffects(lastChild, t.getCompiler())) {
        // Improve handling of function calls
        String msg = "This code lacks side-effects. Is there a bug?";
        if (n.isString()) {
          msg = "Is there a missing '+' on the previous line?";
        }

        t.getCompiler().report(
            t.makeError(n, level, USELESS_CODE_ERROR, msg));
        // TODO(johnlenz): determine if it is necessary to
        // try to protect side-effect free statements as well.
        if (!NodeUtil.isStatement(n)) {
          problemNodes.add(n);
        }
      }
    }
  }
}