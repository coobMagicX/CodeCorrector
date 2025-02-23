public void visit(NodeTraversal t, Node n, Node parent) {
    // VOID nodes appear when there are extra semicolons at the BLOCK level.
    if (n.isEmpty() || n.isComma()) {
        return;
    }

    if (parent == null) {
        return;
    }

    // Skip over BLOCK and EXPR_RESULT types as their removal is handled by peephole passes
    if (parent.getType() == Token.EXPR_RESULT || parent.getType() == Token.BLOCK) {
        return;
    }

    // Special handling when node is within a COMMA operation.
    if (parent.getType() == Token.COMMA) {
        Node gramps = parent.getParent();
        if (gramps != null && gramps.isCall() && parent == gramps.getFirstChild()) {
            if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
                return;
            }
        }

        if (n == parent.getLastChild()) {
            Node ancestor = parent;
            while (ancestor != null) {
                int ancestorType = ancestor.getType();
                if (ancestorType == Token.COMMA) {
                    // Continue to search up through commas.
                    ancestor = ancestor.getParent();
                    continue;
                }
                return; // Once we leave comma-chains, end checks for EXPR_RESULT or BLOCK.
            }
        }
    }

    boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
    if (!isResultUsed && !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
        if (n.isQualifiedName() && n.getJSDocInfo() != null) {
            return;
        }

        String msg = "This code lacks side-effects. Is there a bug?";
        if (n.isString()) {
            msg = "Is there a missing '+' on the previous line?";
        } else if (NodeUtil.isSimpleOperatorType(n.getType())) {
            msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";
        }

        t.getCompiler().report(t.makeError(n, CheckLevel.WARNING, USELESS_CODE_ERROR, msg));

        if (!NodeUtil.isStatement(n)) {
            problemNodes.add(n);
        }
    }
}
