public void visit(NodeTraversal t, Node n, Node parent) {
  // VOID nodes appear when there are extra semicolons at the BLOCK level.
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  if (parent.getType() == Token.COMMA && parent.getParent().isCall() && parent == parent.getParent().getFirstChild()) {
    Node gramps = parent.getParent();
    if (n == gramps.getFirstChild() && gramps.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
      return;
    }
  }

  // This no-op statement was there so that JSDoc information could
  // be attached to the name. This check should not complain about it.
  if (n == parent.getLastChild()) {
    for (Node an : parent.getAncestors()) {
      int ancestorType = an.getType();
      if (ancestorType == Token.COMMA)
        continue;
      if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK) {
        return;
      }
      break;
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() ||
         n == parent.getFirstChild().getNext().getNext())) {
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
    
    // Check for a call to goog.reflect.sinkValue that needs to be removed
    if (n.isCall()) {
      Node target = n.getFirstChild();
      if (target.isName() && target.getString().equals(PROTECTOR_FN)) {
        Node expr = n.getLastChild();
        n.detachChildren();
        parent.replaceChild(n, expr);
      }
    }

    // If it's not a statement and it doesn't have side effects, add it to problemNodes
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}