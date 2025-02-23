public void visit(NodeTraversal t, Node n, Node parent) {
    // VOID nodes and commas are not of concern for side effects
    if (n.isEmpty() || n.isComma()) {
        return;
    }

    // Return immediately if there's no parent
    if (parent == null) {
        return;
    }

    // Special cases for structures where removal is managed differently or not of concern
    if (parent.getType() == Token.COMMA) {
        Node gramps = parent.getParent();
        if (gramps.isCall() && parent == gramps.getFirstChild()) {
            // Cases where function calls like eval might be significant and should not report an error directly
            if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
                return;
            }
        }
        // For sequence of expressions where the last expression's result holds
        // Need to be careful not to skip crucial checks
        if (n != parent.getLastChild()) {
            checkUselessCode(t, n, true);  // Assume sequence expression here for the generic check
        }
    } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
        if (parent.getType() == Token.FOR && parent.getChildCount() == 4) {
            // For-loop specifics are already handled and has special cases with side-effects
            // Consider no further checks if it's part of for-loop initiation or increment expressions.
            if (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()) {
                return;
            }
        } else {
            checkUselessCode(t, n, false); // Normal context checks
        }
    }

    // General case checking
    checkUselessCode(t, n, false);
}

private void checkUselessCode(NodeTraversal t, Node n, boolean isSequenceExpr) {
    boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
    boolean hasSideEffects = NodeUtil.mayHaveSideEffects(n, t.getCompiler());
    boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());

    if (!isResultUsed && (isSimpleOp || !hasSideEffects)) {
        String msg = determineMessage(n, isSimpleOp);
        t.getCompiler().report(t.makeError(n, DiagnosticType.WARNING, "USELESS_CODE_ERROR", msg));
    }

    // If sequence, recursively check each sequence component
    if (isSequenceExpr && n.isComma()) {
        Node child = n.getFirstChild();
        while (child != null) {
            checkUselessCode(t, child, child.getNext() != null);
            child = child.getNext();
        }
    }
}

private String determineMessage(Node n, boolean isSimpleOp) {
    if (isSimpleOp) {
        return "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
    } else if (n.isString()) {
        return "Is there a missing '+' on the previous line?";
    } else {
        return "This code lacks side-effects. Is there a bug?";
    }
}
