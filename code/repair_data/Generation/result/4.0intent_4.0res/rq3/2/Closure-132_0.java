private Node tryMinimizeIf(Node n) {
    Node parent = n.getParent();
    Node cond = n.getFirstChild();

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
            n.removeChild(cond);
            Node and = IR.and(cond, expr.removeFirstChild()).srcref(n);
            Node newExpr = NodeUtil.newExpr(and);
            parent.replaceChild(n, newExpr);
            reportCodeChange();
            return newExpr;
        }
        if (NodeUtil.isStatementBlock(thenBranch) && thenBranch.hasOneChild()) {
            Node innerIf = thenBranch.getFirstChild();
            if (innerIf.isIf()) {
                Node innerCond = innerIf.getFirstChild();
                Node innerThenBranch = innerCond.getNext();
                Node innerElseBranch = innerThenBranch.getNext();
                if (innerElseBranch == null && !(isLowerPrecedenceInExpression(cond, AND_PRECEDENCE) &&
                                                 isLowerPrecedenceInExpression(innerCond, AND_PRECEDENCE))) {
                    n.detachChildren();
                    n.addChildToBack(IR.and(cond, innerCond.detachFromParent()).srcref(cond));
                    n.addChildrenToBack(innerThenBranch.detachFromParent());
                    reportCodeChange();
                    return n;
                }
            }
        }
        return n;
    }

    tryRemoveRepeatedStatements(n);

    if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
        n.replaceChild(cond, cond.removeFirstChild());
        n.removeChild(thenBranch);
        n.addChildToBack(thenBranch);
        reportCodeChange();
        return n;
    }

    if (isReturnExpressBlock(thenBranch) && isReturnExpressBlock(elseBranch)) {
        Node thenExpr = getBlockReturnExpression(thenBranch);
        Node elseExpr = getBlockReturnExpression(elseBranch);
        n.removeChild(cond);
        thenExpr.detachFromParent();
        elseExpr.detachFromParent();
        Node returnNode = IR.returnNode(IR.hook(cond, thenExpr, elseExpr).srcref(n));
        parent.replaceChild(n, returnNode);
        reportCodeChange();
        return returnNode;
    }

    return n;
}