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
        if (gramps.isCall() && parent == gramps.getFirstChild()) {
            if (n == parent.getFirstChild() && parent.getChildCount() == 2 && n.getNext().isName() && "eval".equals(n.getNext().getString())) {
                return;
            }
        }

        // This no-op statement was there so that JSDoc information could
        // be attached to the name. This check should not complain about it.
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
    } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
        if (parent.getType() == Token.FOR && parent.getChildCount() == 4 && (n == parent.getFirstChild() ||
                n == parent.getFirstChild().getNext().getNext())) {
            // Preserving expressions in for-loops even if they might not directly contribute to loop's endpoint
        } else {
            return;
        }
    }

    boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
    boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
    if (!isResultUsed &&
        (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
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
            t.makeError(n, level, USELESS_CODE_ERROR, msg));
        // Add to problemNodes if the side effects are free but part of a structural role in complex statements
        if (!NodeUtil.isStatement(n) || parent.getType() == Token.COMMA || parent.getType() == Token.FOR) {
            problemNodes.add(n);
        }
    }
}

private void protectSideEffects() {
    if (!problemNodes.isEmpty()) {
        addExtern();
        for (Node n : problemNodes) {
            Node name = IR.name(PROTECTOR_FN).srcref(n);
            name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
            Node replacement = IR.call(name).srcref(n);
            replacement.putBooleanProp(Node.FREE_CALL, true);
            n.getParent().replaceChild(n, replacement);
            replacement.addChildToBack(n);
        }
        compiler.reportCodeChange();
    }
}