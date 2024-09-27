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
        // Keep opportunities for CollapseProperties such as
        // a.longIdentifier || a.longIdentifier = ... -> var a = ...;
        // until CollapseProperties has been run.
        return n;
      }

      if (cond.isNot() && !hasSideEffects(cond) && !hasSideEffects(expr.getFirstChild())) {
        // if(!x)bar(); -> x||bar();
        if (isLowerPrecedenceInExpression(cond, OR_PRECEDENCE) &&
            isLowerPrecedenceInExpression(expr.getFirstChild(),
                OR_PRECEDENCE)) {
          // It's not okay to add two sets of parentheses.
          return n;
        }

        Node or = IR.or(
            cond.removeFirstChild(),
            expr.removeFirstChild()).srcref(n);
        Node newExpr = NodeUtil.newExpr(or);
        parent.replaceChild(n, newExpr);
        reportCodeChange();

        return newExpr;
      }

      // if(x)foo(); -> x&&foo();
      if (!hasSideEffects(cond) && !hasSideEffects(expr.getFirstChild())) {
        if (isLowerPrecedenceInExpression(cond, AND_PRECEDENCE) &&
            isLowerPrecedenceInExpression(expr.getFirstChild(),
                AND_PRECEDENCE)) {
          // One additional set of parentheses is worth the change even if
          // there is no immediate code size win. However, two extra pair of
          // {}, we would have to think twice. (unless we know for sure the
          // we can further optimize its parent.
          return n;
        }

        n.removeChild(cond);
        Node and = IR.and(cond, expr.removeFirstChild()).srcref(n);
        Node newExpr = NodeUtil.newExpr(and);
        parent.replaceChild(n, newExpr);
        reportCodeChange();

        return newExpr;
      }
    }
    return n;
  }

  tryRemoveRepeatedStatements(n);

  // if(!x)foo();else bar(); -> if(x)bar();else foo();
  if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
    n.replaceChild(cond, cond.removeFirstChild());
    Node temp = thenBranch;
    n.removeChild(thenBranch);
    n.addChildToBack(elseBranch);
    n.addChildToBack(temp);
    reportCodeChange();
    return n;
  }

  // if(x)return 1;else return 2; -> return x?1:2;
  if (isReturnExpressBlock(thenBranch) && isReturnExpressBlock(elseBranch)) {
    Node thenExpr = getBlockReturnExpression(thenBranch);
    Node elseExpr = getBlockReturnExpression(elseBranch);
    n.removeChild(cond);
    thenExpr.detachFromParent();
    elseExpr.detachFromParent();

    Node returnNode = IR.returnNode(
                          IR.hook(cond, thenExpr, elseExpr)
                              .srcref(n));
    parent.replaceChild(n, returnNode);
    reportCodeChange();
    return returnNode;
  }

  // Other conditions...
  return n;
}