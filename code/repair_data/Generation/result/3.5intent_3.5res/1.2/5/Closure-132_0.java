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

      if (cond.isNot()) {
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
    } else {

      // Try to combine two IF-ELSE
      if (NodeUtil.isStatementBlock(thenBranch) &&
          thenBranch.hasOneChild()) {
        Node innerIf = thenBranch.getFirstChild();

        if (innerIf.isIf()) {
          Node innerCond = innerIf.getFirstChild();
          Node innerThenBranch = innerCond.getNext();
          Node innerElseBranch = innerThenBranch.getNext();

          if (innerElseBranch == null &&
               !(isLowerPrecedenceInExpression(cond, AND_PRECEDENCE) &&
                 isLowerPrecedenceInExpression(innerCond, AND_PRECEDENCE))) {
            n.detachChildren();
            n.addChildToBack(
                IR.and(
                    cond,
                    innerCond.detachFromParent())
                    .srcref(cond));
            n.addChildrenToBack(innerThenBranch.detachFromParent());
            reportCodeChange();
            // Not worth trying to fold the current IF-ELSE into && because
            // the inner IF-ELSE wasn't able to be folded into && anyways.
            return n;
          }
        }
      }
    }

    return n;
  }

  /* TODO(dcc) This modifies the siblings of n, which is undesirable for a
   * peephole optimization. This should probably get moved to another pass.
   */
  tryRemoveRepeatedStatements(n);

  // if(!x)foo();else bar(); -> if(x)bar();else foo();
  // An additional set of curly braces isn't worth it.
  if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
    n.replaceChild(cond, cond.removeFirstChild());
    n.removeChild(thenBranch);
    n.addChildToBack(thenBranch);
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

    // note - we ignore any cases with "return;", technically this
    // can be converted to "return undefined;" or some variant, but
    // that does not help code size.
    Node returnNode = IR.returnNode(
                          IR.hook(cond, thenExpr, elseExpr)
                              .srcref(n));
    parent.replaceChild(n, returnNode);
    reportCodeChange();
    return returnNode;
  }

  boolean thenBranchIsExpressionBlock = isFoldableExpressBlock(thenBranch);
  boolean elseBranchIsExpressionBlock = isFoldableExpressBlock(elseBranch);

  if (thenBranchIsExpressionBlock && elseBranchIsExpressionBlock) {
    Node thenOp = getBlockExpression(thenBranch).getFirstChild();
    Node elseOp = getBlockExpression(elseBranch).getFirstChild();
    if (thenOp.getType() == elseOp.getType()) {
      // if(x)a=1;else a=2; -> a=x?1:2;
      if (NodeUtil.isAssignmentOp(thenOp)) {
        Node lhs = thenOp.getFirstChild();
        if (areNodesEqualForInlining(lhs, elseOp.getFirstChild()) &&
            // if LHS has side effects, don't proceed [since the optimization
            // evaluates LHS before cond]
            // NOTE - there are some circumstances where we can
            // proceed even if there are side effects...
            !mayEffectMutableState(lhs)) {

          n.removeChild(cond);
          Node assignName = thenOp.removeFirstChild();
          Node thenExpr = thenOp.removeFirstChild();
          Node elseExpr = elseOp.getLastChild();
          elseOp.removeChild(elseExpr);

          Node hookNode = IR.hook(cond, thenExpr, elseExpr).srcref(n);
          Node assign = new Node(thenOp.getType(), assignName, hookNode)
                            .srcref(thenOp);
          Node expr = NodeUtil.newExpr(assign);
          parent.replaceChild(n, expr);
          reportCodeChange();

          return expr;
        }
      }
    }
    // if(x)foo();else bar(); -> x?foo():bar()
    n.removeChild(cond);
    thenOp.detachFromParent();
    elseOp.detachFromParent();
    Node expr = IR.exprResult(
        IR.hook(cond, thenOp, elseOp).srcref(n));
    parent.replaceChild(n, expr);
    reportCodeChange();
    return expr;
  }

  boolean thenBranchIsVar = isVarBlock(thenBranch);
  boolean elseBranchIsVar = isVarBlock(elseBranch);

  // if(x)var y=1;else y=2  ->  var y=x?1:2
  if (thenBranchIsVar && elseBranchIsExpressionBlock &&
      getBlockExpression(elseBranch).getFirstChild().isAssign()) {

    Node var = getBlockVar(thenBranch);
    Node elseAssign = getBlockExpression(elseBranch).getFirstChild();

    Node name1 = var.getFirstChild();
    Node maybeName2 = elseAssign.getFirstChild();

    if (name1.hasChildren()
        && maybeName2.isName()
        && name1.getString().equals(maybeName2.getString())) {
      Node thenExpr = name1.removeChildren();
      Node elseExpr = elseAssign.getLastChild().detachFromParent();
      cond.detachFromParent();
      Node hookNode = IR.hook(cond, thenExpr, elseExpr)
                          .srcref(n);
      var.detachFromParent();
      name1.addChildrenToBack(hookNode);
      parent.replaceChild(n, var);
      reportCodeChange();
      return var;
    }

  // if(x)y=1;else var y=2  ->  var y=x?1:2
  } else if (elseBranchIsVar && thenBranchIsExpressionBlock &&
      getBlockExpression(thenBranch).getFirstChild().isAssign()) {

    Node var = getBlockVar(elseBranch);
    Node thenAssign = getBlockExpression(thenBranch).getFirstChild();

    Node maybeName1 = thenAssign.getFirstChild();
    Node name2 = var.getFirstChild();

    if (name2.hasChildren()
        && maybeName1.isName()
        && maybeName1.getString().equals(name2.getString())) {
      Node thenExpr = thenAssign.getLastChild().detachFromParent();
      Node elseExpr = name2.removeChildren();
      cond.detachFromParent();
      Node hookNode = IR.hook(cond, thenExpr, elseExpr)
                          .srcref(n);
      var.detachFromParent();
      name2.addChildrenToBack(hookNode);
      parent.replaceChild(n, var);
      reportCodeChange();

      return var;
    }
  }

  return n;
}

private Node tryMinimizeCondition(Node n) {
  Node parent = n.getParent();

  switch (n.getType()) {
    case Token.NOT:
      Node first = n.getFirstChild();
      switch (first.getType()) {
        case Token.NOT: {
            Node newRoot = first.removeFirstChild();
            parent.replaceChild(n, newRoot);
            reportCodeChange();
            // No need to traverse, tryMinimizeCondition is called on the
            // NOT children are handled below.
            return newRoot;
          }
        case Token.AND:
        case Token.OR: {
            // !(!x && !y) --> x || y
            // !(!x || !y) --> x && y
            // !(!x && y) --> x || !y
            // !(!x || y) --> x && !y
            // !(x && !y) --> !x || y
            // !(x || !y) --> !x && y
            // !(x && y) --> !x || !y
            // !(x || y) --> !x && !y
            Node leftParent = first.getFirstChild();
            Node rightParent = first.getLastChild();
            Node left, right;

            // Check special case when such transformation cannot reduce
            // due to the added ()
            // It only occurs when both of expressions are not NOT expressions
            if (!leftParent.isNot()
                && !rightParent.isNot()) {
              // If an expression has higher precedence than && or ||,
              // but lower precedence than NOT, an additional () is needed
              // Thus we do not preceed
              int op_precedence = NodeUtil.precedence(first.getType());
              if ((isLowerPrecedence(leftParent, NOT_PRECEDENCE)
                  && isHigherPrecedence(leftParent, op_precedence))
                  || (isLowerPrecedence(rightParent, NOT_PRECEDENCE)
                  && isHigherPrecedence(rightParent, op_precedence))) {
                return n;
              }
            }

            if (leftParent.isNot()) {
              left = leftParent.removeFirstChild();
            } else {
              leftParent.detachFromParent();
              left = IR.not(leftParent).srcref(leftParent);
            }
            if (rightParent.isNot()) {
              right = rightParent.removeFirstChild();
            } else {
              rightParent.detachFromParent();
              right = IR.not(rightParent).srcref(rightParent);
            }

            int newOp = (first.isAnd()) ? Token.OR : Token.AND;
            Node newRoot = new Node(newOp, left, right);
            parent.replaceChild(n, newRoot);
            reportCodeChange();
            // No need to traverse, tryMinimizeCondition is called on the
            // AND and OR children below.
            return newRoot;
          }

         default:
           TernaryValue nVal = NodeUtil.getPureBooleanValue(first);
           if (nVal != TernaryValue.UNKNOWN) {
             boolean result = nVal.not().toBoolean(true);
             int equivalentResult = result ? 1 : 0;
             return maybeReplaceChildWithNumber(n, parent, equivalentResult);
           }
      }
      // No need to traverse, tryMinimizeCondition is called on the NOT
      // children in the general case in the main post-order traversal.
      return n;

    case Token.OR:
    case Token.AND: {
      Node left = n.getFirstChild();
      Node right = n.getLastChild();

      // Because the expression is in a boolean context minimize
      // the children, this can't be done in the general case.
      left = tryMinimizeCondition(left);
      right = tryMinimizeCondition(right);

      // Remove useless conditionals
      // Handle four cases:
      //   x || false --> x
      //   x || true  --> true
      //   x && true --> x
      //   x && false  --> false
      TernaryValue rightVal = NodeUtil.getPureBooleanValue(right);
      if (NodeUtil.getPureBooleanValue(right) != TernaryValue.UNKNOWN) {
        int type = n.getType();
        Node replacement = null;
        boolean rval = rightVal.toBoolean(true);

        // (x || FALSE) => x
        // (x && TRUE) => x
        if (type == Token.OR && !rval ||
            type == Token.AND && rval) {
          replacement = left;
        } else if (!mayHaveSideEffects(left)) {
          replacement = right;
        }

        if (replacement != null) {
          n.detachChildren();
          parent.replaceChild(n, replacement);
          reportCodeChange();
          return replacement;
        }
      }
      return n;
    }

    case Token.HOOK: {
      Node condition = n.getFirstChild();
      Node trueNode = n.getFirstChild().getNext();
      Node falseNode = n.getLastChild();

      // Because the expression is in a boolean context minimize
      // the result children, this can't be done in the general case.
      // The condition is handled in the general case in #optimizeSubtree
      trueNode = tryMinimizeCondition(trueNode);
      falseNode = tryMinimizeCondition(falseNode);

      // Handle four cases:
      //   x ? true : false --> x
      //   x ? false : true --> !x
      //   x ? true : y     --> x || y
      //   x ? y : false    --> x && y
      Node replacement = null;
      TernaryValue trueNodeVal = NodeUtil.getPureBooleanValue(trueNode);
      TernaryValue falseNodeVal = NodeUtil.getPureBooleanValue(falseNode);
      if (trueNodeVal == TernaryValue.TRUE
          && falseNodeVal == TernaryValue.FALSE) {
        // Remove useless conditionals, keep the condition
        condition.detachFromParent();
        replacement = condition;
      } else if (trueNodeVal == TernaryValue.FALSE
          && falseNodeVal == TernaryValue.TRUE) {
        // Remove useless conditionals, keep the condition
        condition.detachFromParent();
        replacement = IR.not(condition);
      } else if (trueNodeVal == TernaryValue.TRUE) {
        // Remove useless true case.
        n.detachChildren();
        replacement = IR.or(condition, falseNode);
      } else if (falseNodeVal == TernaryValue.FALSE) {
        // Remove useless false case
        n.detachChildren();
        replacement = IR.and(condition, trueNode);
      }

      if (replacement != null) {
        parent.replaceChild(n, replacement);
        n = replacement;
        reportCodeChange();
      }

      return n;
    }

    default:
      // while(true) --> while(1)
      TernaryValue nVal = NodeUtil.getPureBooleanValue(n);
      if (nVal != TernaryValue.UNKNOWN) {
        boolean result = nVal.toBoolean(true);
        int equivalentResult = result ? 1 : 0;
        return maybeReplaceChildWithNumber(n, parent, equivalentResult);
      }
      // We can't do anything else currently.
      return n;
  }
}