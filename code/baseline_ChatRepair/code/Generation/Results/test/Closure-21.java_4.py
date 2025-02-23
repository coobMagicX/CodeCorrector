public void visit(NodeTraversal t, Node n, Node parent) {
    // Handling redundant expressions and semicolons.
    if (n.isEmpty() || n.isComma()) {
        return;
    }

    if (parent == null) {
        return;
    }

    // Skip particular nodes that don't need evaluation here.
    if (n.isExprResult() || n.isQualifiedName() && n.getJSDocInfo() != null) {
        return;
    }

    boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
    if (!isResultUsed && !NodeUtil.mayHaveSideEffects(n, t.getCompiler()) && isUselessCondition(parent, n)) {
        String msg = "This code lacks side-effects and the result of the expression is not used.";
        t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, msg));
        if (!NodeUtil.isStatement(n)) {
            problemNodes.add(n);
        }
    }
}

private boolean isUselessCondition(Node parent, Node n) {
    // Checks if the expression is part of a condition that doesn't use its result.
    switch (parent.getType()) {
        case Token.IF:
        case Token.WHILE:
        case Token.FOR:
            // In these cases, the condition is the first child.
            return parent.getFirstChild() == n;
        case Token.DO:
            // In a do-while loop, the condition is the last child.
            return parent.getLastChild() == n;
        default:
            return false;
    }
}
