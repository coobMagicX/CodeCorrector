private Node tryMinimizeIf(Node n) {
  Node parent = n.getParent();

  Node cond = n.getFirstChild();

  /* If the condition is a literal, we'll let other
   * optimizations try to remove useless code.
   */
  if (NodeUtil.isLiteralValue(cond, true)) {
    return n;
  }

  Node thenBranch = cond.getNext();
  Node elseBranch = thenBranch.getNext();

  if (elseBranch == null) {
    if (isFoldableExpressBlock(thenBranch)) {
      Node expr = getBlockExpression(thenBranch);
      if (!late && isPropertyAssignmentInExpression(expr)) {
        return n;
      }

      Node newExpr;
      if (cond.isNot()) {
        // Correctly handle negation and side effects
        Node convertedCond = tryMinimizeCondition(cond).removeFirstChild();
        Node convertedExpr = expr.removeFirstChild();
        Node or = IR.or(convertedCond, convertedExpr).srcrefIfMissing(n);
        newExpr = NodeUtil.newExpr(or);
      } else {
        Node and = IR.and(cond, expr.removeFirstChild()).srcrefIfMissing(n);
        newExpr = NodeUtil.newExpr(and);
      }
      parent.replaceChild(n, newExpr);
      reportCodeChange();
      return newExpr;
    } else {
      return tryCombineIfElse(n, cond, thenBranch);
    }
  } else {
    return tryConvertIfElseToTernary(n, cond, thenBranch, elseBranch, parent);
  }
}

private Node tryCombineIfElse(Node n, Node cond, Node thenBranch) {
  // This method is a placeholder for the logic to combine nested IF statements,
  // which should be implemented based on the existing code logic in the problematic section.
  return n;
}

private Node tryConvertIfElseToTernary(Node n, Node cond, Node thenBranch, Node elseBranch, Node parent) {
  if (isReturnExpressBlock(thenBranch) && isReturnExpressBlock(elseBranch)) {
    Node thenExpr = getBlockReturnExpression(thenBranch);
    Node elseExpr = getBlockReturnExpression(elseBranch);
    Node returnNode = IR.returnNode(IR.hook(cond, thenExpr, elseExpr).srcref(n));
    parent.replaceChild(n, returnNode);
    reportCodeChange();
    return returnNode;
  }

  boolean thenBranchIsExpressionBlock = isFoldableExpressBlock(thenBranch);
  boolean elseBranchIsExpressionBlock = isFoldableExpressBlock(elseBranch);

  if (thenBranchIsExpressionBlock && elseBranchIsExpressionBlock) {
    Node thenOp = getBlockExpression(thenBranch).getFirstChild();
    Node elseOp = getBlockExpression(elseBranch).getFirstChild();
    Node expr = IR.exprResult(IR.hook(cond, thenOp, elseOp).srcref(n));
    parent.replaceChild(n, expr);
    reportCodeChange();
    return expr;
  }

  return n;
}