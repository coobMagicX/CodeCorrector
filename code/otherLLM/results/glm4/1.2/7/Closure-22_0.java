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
    }

    // Repair: Check for side effects with JSCOMPILER_PRESERVE
    if (NodeUtil.mayHaveSideEffects(n, t.getCompiler()) ||
        parent.getType() == Token.FOR) { // Preserve FOR loops as they may have side effects
      t.getCompiler().preserve(n);
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() ||
         n == parent.getFirstChild().getNext().getNext())) {
      // Repair: Check for side effects with JSCOMPILER_PRESERVE
      t.getCompiler().preserve(n);
    } else {
      return;
    }
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
  if (!isResultUsed &&
      (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
    if (n.isQualifiedName() && n.getJSDocInfo() != null) {
      return;
    } else if (n.isExprResult()) {
      return;
    }
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
  }

  // Repair: Apply existing method for handling calls with specific conditions
  if (n.isCall()) {
    Node target = n.getFirstChild();
    // TODO(johnlenz): add this to the coding convention
    // so we can remove goog.reflect.sinkValue as well.
    if (target.isName() && target.getString().equals(PROTECTOR_FN)) {
      Node expr = n.getLastChild();
      n.detachChildren();
      parent.replaceChild(n, expr);
    }
  }
}