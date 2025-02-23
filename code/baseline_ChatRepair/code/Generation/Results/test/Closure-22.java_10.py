public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  if (parent.getType() == Token.COMMA) {
    Node gramps = parent.getParent();
    if (gramps.isCall() && parent == gramps.getFirstChild()) {
      if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
        return;
      }
    }

    Node lastChild = parent;
    while (lastChild.getNext() != null) {
      lastChild = lastChild.getNext();
    }
    if (n == lastChild) {
      return;
    }
  } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    // Handling for loops or other structures
    if (parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext())) {
      return;
    }
    return;
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
  if (!isResultUsed && (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
    if (n.isQualifiedName() && n.getJSDocInfo() != null) {
      return;
    } else if (n.isExprResult()) {
      return;
    }
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (isSimpleOp) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }

    t.getCompiler().report(t.makeError(n, level, USELESS_CODE_ERROR, msg));
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
