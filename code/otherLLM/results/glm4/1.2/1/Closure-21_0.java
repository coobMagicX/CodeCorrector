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
  
  // Check for comma expressions and sequences in parentheses
  if (parent.getType() == Token.COMMA && n != parent.getLastChild()) {
    if (!isResultUsed) {
      return;
    }
    
    // Additional check to ensure the node is not part of a larger expression that would still have side effects.
    boolean hasSideEffects = false;
    for (Node sibling : n.getParent().getChildren()) {
      if (sibling != n && NodeUtil.mayHaveSideEffects(sibling, t.getCompiler())) {
        hasSideEffects = true;
        break;
      }
    }
    
    if (!hasSideEffects) {
      String msg = "The result of a comma expression is not being used.";
      t.getCompiler().report(
          t.makeError(n, level, USELESS_CODE_ERROR, msg));
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    // Check for other cases where the node might lack side effects
    boolean hasSideEffects = false;
    Node firstChild = n.getFirstChild();
    while (firstChild != null) {
      if (NodeUtil.mayHaveSideEffects(firstChild, t.getCompiler())) {
        hasSideEffects = true;
        break;
      }
      firstChild = firstChild.getNext();
    }

    if (!hasSideEffects) {
      String msg = "This code lacks side-effects. Is there a bug?";
      t.getCompiler().report(
          t.makeError(n, level, USELESS_CODE_ERROR, msg));
      
      // Additional check for statements without side effects
      if (!NodeUtil.isStatement(n)) {
        problemNodes.add(n);
      }
    }
  } else if (
      (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
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
}