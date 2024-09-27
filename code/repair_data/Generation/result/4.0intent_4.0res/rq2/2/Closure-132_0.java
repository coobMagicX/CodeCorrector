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

      if (cond.isNot()) {
        if (isLowerPrecedenceInExpression(cond, OR_PRECEDENCE) &&
            isLowerPrecedenceInExpression(expr.getFirstChild(), OR_PRECEDENCE)) {
          return n;
        }

        Node or = IR.or(cond.removeFirstChild(), expr.removeFirstChild()).srcref(n);
        Node newExpr = NodeUtil.newExpr(or);
        parent.replaceChild(n, newExpr);
        reportCodeChange();
        return newExpr;
      }

      if (isLowerPrecedenceInExpression(cond, AND_PRECEDENCE) &&
          isLowerPrecedenceInExpression(expr.getFirstChild(), AND_PRECEDENCE)) {
        return n;
      }

      Node and = IR.and(cond.detachFromParent(), expr.removeFirstChild()).srcref(n);
      Node newExpr = NodeUtil.newExpr(and);
      parent.replaceChild(n, newExpr);
      reportCodeChange();
      return newExpr;
    }
  } else {
    tryRemoveRepeatedStatements(n);

    if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
      Node invertedCond = cond.getFirstChild().detachFromParent();
      parent.replaceChild(n, IR.ifNode(invertedCond, elseBranch.detachFromParent(), thenBranch.detachFromParent()).srcref(n));
      reportCodeChange();
      return n;
    }

    if (isReturnExpressBlock(thenBranch) && isReturnExpressBlock(elseBranch)) {
      Node thenExpr = getBlockReturnExpression(thenBranch);
      Node elseExpr = getBlockReturnExpression(elseBranch);
      Node returnNode = IR.returnNode(IR.hook(cond.detachFromParent(), thenExpr.detachFromParent(), elseExpr.detachFromParent()).srcref(n));
      parent.replaceChild(n, returnNode);
      reportCodeChange();
      return returnNode;
    }

    boolean thenBranchIsExpressionBlock = isFoldableExpressBlock(thenBranch);
    boolean elseBranchIsExpressionBlock = isFoldableExpressBlock(elseBranch);

    if (thenBranchIsExpressionBlock && elseBranchIsExpressionBlock) {
      Node thenOp = getBlockExpression(thenBranch).getFirstChild();
      Node elseOp = getBlockExpression(elseBranch).getFirstChild();
      if (thenOp.getType() == elseOp.getType() && NodeUtil.isAssignmentOp(thenOp)) {
        Node lhs = thenOp.getFirstChild();
        if (areNodesEqualForInlining(lhs, elseOp.getFirstChild()) && !mayEffectMutableState(lhs)) {
          Node assign = new Node(thenOp.getType(), lhs.detachFromParent(), IR.hook(cond.detachFromParent(), thenOp.getLastChild().detachFromParent(), elseOp.getLastChild().detachFromParent()).srcref(n));
          Node expr = NodeUtil.newExpr(assign);
          parent.replaceChild(n, expr);
          reportCodeChange();
          return expr;
        }
      }
      Node expr = IR.exprResult(IR.hook(cond.detachFromParent(), thenOp.detachFromParent(), elseOp.detachFromParent()).srcref(n));
      parent.replaceChild(n, expr);
      reportCodeChange();
      return expr;
    }
  }

  return n;
}