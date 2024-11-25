if (thenBranchIsExpressionBlock && elseBranchIsVar) {
    Node thenOp = getBlockExpression(thenBranch).getFirstChild();
    Node var = getBlockVar(elseBranch);
    if (thenOp.getType() == Node.ASSIGN) {
        Node lhs = thenOp.getFirstChild();
        if (areNodesEqualForInlining(lhs, var.getFirstChild())) {
            //...
        }
    } else {
        n.removeChild(cond);
        Node expr = IR.hook(cond, getBlockExpression(thenBranch).getFirstChild(), var)
                        .srcref(n);
        parent.replaceChild(n, expr);
        reportCodeChange();
        return expr;
    }
}