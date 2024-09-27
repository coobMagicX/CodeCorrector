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

            // if(x)foo(); -> x&&foo();
            Node or = IR.and(cond, expr.getFirstChild()).srcref(n);
            Node newExpr = NodeUtil.newExpr(or);
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

                    if (innerElseBranch == null) {
                        n.detachChildren();
                        n.addChildToBack(IR.and(cond, innerCond.detachFromParent()).srcref(cond));
                        n.addChildrenToBack(innerThenBranch.detachFromParent());
                        reportCodeChange();
                        return n;
                    }
                }
            }
        }

        return n;
    }

    // if(!x)foo();else bar(); -> x?bar():foo();
    if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
        Node newCond = cond.getFirstChild();
        n.replaceChild(cond, newCond);
        n.removeChild(thenBranch);
        n.addChildToFront(elseBranch);
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

        Node returnNode = IR.returnNode(IR.hook(newCond, thenExpr, elseExpr).srcref(n));
        parent.replaceChild(n, returnNode);
        reportCodeChange();
        return returnNode;
    }

    // if(x)foo();else bar(); -> x?foo():bar();
    if (isFoldableExpressBlock(thenBranch) && isFoldableExpressBlock(elseBranch)) {
        Node thenExpr = getBlockExpression(thenBranch).getFirstChild();
        Node elseExpr = getBlockExpression(elseBranch).getFirstChild();
        n.removeChild(cond);
        thenExpr.detachFromParent();
        elseExpr.detachFromParent();
        Node expr = IR.exprResult(IR.hook(newCond, thenExpr, elseExpr).srcref(n));
        parent.replaceChild(n, expr);
        reportCodeChange();
        return expr;
    }

    return n;
}