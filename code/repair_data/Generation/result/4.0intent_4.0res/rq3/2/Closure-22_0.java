public void visit(NodeTraversal t, Node n, Node parent) {
    // VOID nodes appear when there are extra semicolons at the BLOCK level.
    // I've been unable to think of any cases where this indicates a bug,
    // and apparently some people like keeping these semicolons around,
    // so we'll allow it.
    if (n.isEmpty() || n.isComma()) {
        return;
    }

    if (parent == null) {
        return;
    }

    // Do not try to remove a block or an expr result. We already handle
    // these cases when we visit the child, and the peephole passes will
    // fix up the tree in more clever ways when these are removed.
    if (parent.getType() == Token.COMMA) {
        Node gramps = parent.getParent();
        if (gramps != null && gramps.isCall() && parent == gramps.getFirstChild()) {
            Node firstChild = parent.getFirstChild();
            if (n == firstChild && parent.getChildCount() == 2 && firstChild.getNext().isName() && "eval".equals(firstChild.getNext().getString())) {
                return;
            }
        }
    }

    if (n == parent.getLastChild()) {
        for (Node an : parent.getAncestors()) {
            int ancestorType = an.getType();
            if (ancestorType == Token.COMMA)
                continue;
            if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK)
                return;
            else
                break;
        }
    } 

    if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
        if (parent.getType() == Token.FOR && parent.getChildCount() == 4) {
            Node firstChild = parent.getFirstChild();
            if (n != firstChild && n != firstChild.getNext().getNext()) {
                return;
            }
        } else {
            return;
        }
    }

    boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
    boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
    if (!isResultUsed && (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
        if (n.isQualifiedName() && n.getJSDocInfo() != null) {
            return;
        } else if (n.isExprResult()) {
            return;
        }
        String msg = "This code lacks side-effects. Is there a bug?";
        if (n.isString()) {
            msg = "Is there a missing '+' on the previous line?";
        } else if (isSimpleOp) {
            msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
                    "' operator is not being used.";
        }

        t.getCompiler().report(
                t.makeError(n, CheckLevel.WARNING, CheckNames.USELESS_CODE_ERROR, msg));
        // TODO(johnlenz): determine if it is necessary to
        // try to protect side-effect free statements as well.
        if (!NodeUtil.isStatement(n)) {
            problemNodes.add(n);
        }
    }
}