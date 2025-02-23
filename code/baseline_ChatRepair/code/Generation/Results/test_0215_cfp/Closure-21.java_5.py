public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isEmpty() || n.isComma()) {
        // Allow empty nodes, especially useful for parsing stylized code that uses extra semicolons.
        return;
    }

    if (parent == null) {
        return; // Safety check for root nodes
    }

    if (parent.isExprResult() && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
        // Handle expression results without side effects.
        reportUselessCode(t, n, "Expression result is not used and has no side effects.");
        return;
    }

    // Specific check for no-operation inside if statements.
    if (parent.isIf()) {
        Node condition = parent.getFirstChild();
        Node trueBranch = condition.getNext();
        Node falseBranch = trueBranch.getNext();

        if ((trueBranch != null && trueBranch.isEmpty()) && 
            (falseBranch == null || falseBranch.isEmpty()) && 
            !NodeUtil.mayHaveSideEffects(condition, t.getCompiler())) {
            // This condition checks if both branches of the if statement do nothing.
            reportUselessCode(t, n, "If condition has no side effects and both branches are empty.");
            return;
        }
    }

    if (!NodeUtil.isControlStructure(n) && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
        // General case for nodes without side effects.
        reportUselessCode(t, n, "Code lacks side-effects and result of the expression is not used.");
    }
}

private void reportUselessCode(NodeTraversal t, Node n, String message) {
    t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, message));
    if (!NodeUtil.isStatement(n)) {
        problemNodes.add(n);
    }
}
