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
  
    if (n == parent.getLastChild()) {
      for (Node an : NodeUtil.getAncestors(parent)) {
        int ancestorType = an.getType();
        if (ancestorType == Token.COMMA)
          continue;
        if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK)
          return;
        else
          break;
      }
    }
  }

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);

  if (n.isExprResult()) {
    Node child = n.getFirstChild();
    if (child.isCall() || nodeMayHaveSideEffects(child, t.getCompiler())) {
      return;
    }
  }

  // Process other nodes for side effects and useless code
  if (!isResultUsed && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    if (n.isQualifiedName() && n.getJSDocInfo() != null) {
      return;
    }
    String msg = "This code lacks side-effects. Is there a bug?";
    if (n.isString()) {
      msg = "Is there a missing '+' on the previous line?";
    } else if (NodeUtil.isSimpleOperatorType(n.getType())) {
      msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    }

    t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, msg));

    if (!NodeUtil.isStatement(n)) {
      problemNodes.add(n);
    }
  }
}
