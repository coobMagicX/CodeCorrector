private Node tryMinimizeIf(Node n) {
  Node parent = n.getParent();
  Node cond = n.getFirstChild();
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

      Node and = IR.and(cond, expr.removeFirstChild()).srcref(n);
      Node newExpr = NodeUtil.newExpr(and);
      parent.replaceChild(n, newExpr);
      reportCodeChange();

      return newExpr;
    }
    return n;
  }

  if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
    Node notChild = cond.getFirstChild();
    if (NodeUtil.mayHaveSideEffects(notChild)) {
      return n;  // We cannot change the order of operations if it may have side effects.
    }
    n.replaceChild(cond, notChild);
    Node temp = thenBranch;
    n.removeChild(thenBranch);
    n.addChildToBack(temp);
    reportCodeChange();
    return n;
  }

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

    if (thenOp.getType() == elseOp.getType() && NodeUtil.isAssignmentOp(thenOp)) {
      Node lhs = thenOp.getFirstChild();
      if (areNodesEqualForInlining(lhs, elseOp.getFirstChild()) && !mayEffectMutableState(lhs)) {
        Node assignName = thenOp.removeFirstChild();
        Node thenExpr = thenOp.removeFirstChild();
        Node elseExpr = elseOp.getLastChild().detachFromParent();

        Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);
        Node assign = new Node(thenOp.getType(), assignName, hookNode).srcref(thenOp);
        Node expr = NodeUtil.newExpr(assign);
        parent.replaceChild(n, expr);
        reportCodeChange();

        return expr;
      }
    }

    Node expr = IR.exprResult(IR.hook(cond, thenOp, elseOp).srcref(n));
    parent.replaceChild(n, expr);
    reportCodeChange();
    return expr;
  }

  boolean thenBranchIsVar = isVarBlock(thenBranch);
  boolean elseBranchIsVar = isVarBlock(elseBranch);

  if (thenBranchIsVar && elseBranchIsExpressionBlock && getBlockExpression(elseBranch).getFirstChild().isAssign()) {
    Node var = getBlockVar(thenBranch);
    Node elseAssign = getBlockExpression(elseBranch).getFirstChild();

    Node name1 = var.getFirstChild();
    Node maybeName2 = elseAssign.getFirstChild();

    if (name1.hasChildren() && maybeName2.isName() && name1.getString().equals(maybeName2.getString())) {
      Node thenExpr = name1.removeChildren();
      Node elseExpr = elseAssign.getLastChild().detachFromParent();
      Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);

      name1.addChildrenToBack(hookNode);
      parent.replaceChild(n, var);
      reportCodeChange();
      return var;
    }
  } else if (elseBranchIsVar && thenBranchIsExpressionBlock && getBlockExpression(thenBranch).getFirstChild().isAssign()) {
    Node var = getBlockVar(elseBranch);
    Node thenAssign = getBlockExpression(thenBranch).getFirstChild();

    Node maybeName1 = thenAssign.getFirstChild();
    Node name2 = var.getFirstChild();

    if (name2.hasChildren() && maybeName1.isName() && maybeName1.getString().equals(name2.getString())) {
      Node thenExpr = thenAssign.getLastChild().detachFromParent();
      Node elseExpr = name2.removeChildren();
      Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);

      name2.addChildrenToBack(hookNode);
      parent.replaceChild(n, var);
      reportCodeChange();

      return var;
    }
  }

  return n;
}