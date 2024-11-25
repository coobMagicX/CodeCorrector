public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  if (parent == null) {
    return;
  }

  if (parent.getType() == Token.COMMA && parent.getParent().isCall() && parent == parent.getParent().getFirstChild()) {
    Node grandParent = parent.getParent();
    if (grandParent.getFirstChild() == parent && parent.getChildCount() == 2 && grandParent.getFirstChild().getNext().isName() && "eval".equals(grandParent.getFirstChild().getNext().getString())) {
      return;
    }
  }

  if (n == parent.getLastChild()) {
    for (Node ancestor : parent.getAncestors()) {
      int ancestorType = ancestor.getType();
      if (ancestorType != Token.COMMA) continue;
      if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK)
        return;
      else
        break;
    }
  } else if (parent.getType() != Token.COMMA && parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
    if (parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext())) {
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
    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}