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
    if (isResultUsed) {
      return;
    }
    if (n == parent.getLastChild()) {
      for (Node an : parent.getAncestors()) {
        int ancestorType = an.getType();
        if (ancestorType == Token.COMMA) continue;
        if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK) return;
        else break;
      }
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    // Correctly identify and report errors for simple operators that lack usage of their result.
    if (!isResultUsed && isSimpleOp) {
      String msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
          "' operator is not being used.";
      t.getCompiler().report(
          t.makeError(n, level, USELESS_CODE_ERROR, msg));
      return;
    }
    if (! (parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
      return;
    }
  }

  // Check and adjust the logic for handling semicolons, which may indicate extraneous code but should not cause an error in this context.
  if (!NodeUtil.isStatement(n)) {
    String msg = "This node is not a statement and might be extraneous.";
    t.getCompiler().report(t.makeError(n, level, USELESS_CODE_ERROR, msg));
    problemNodes.add(n);
    return;
  }

  // Confirm that compiler preservation tokens are correctly added when necessary to preserve the original code structure.
  if (NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    }
    
    t.getCompiler().report(
        t.makeError(n, level, USELESS_CODE_ERROR, msg));
  } else {
    // We assume here that if there is no side-effect, we need to check for compiler preservation tokens.
    NodeUtil.checkForPreservationTokens(n);
  }
}