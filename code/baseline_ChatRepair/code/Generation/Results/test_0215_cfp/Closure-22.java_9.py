boolean isSequenceWithOnlyLastValueUsed = false;

if (parent.getType() == Token.COMMA) {
    Node ancestor = parent;
    while (ancestor.getType() == Token.COMMA) {
        ancestor = ancestor.getParent();
    }
    // Check if in a sequence, only the last value is used
    isSequenceWithOnlyLastValueUsed = ancestor.getType() == Token.EXPR_RESULT || NodeUtil.isAssignmentOp(ancestor) || ancestor.isVar();

    if (isSequenceWithOnlyLastValueUsed) {
        // Ensure we are at the last operation in the sequence
        Node lastChild = parent;
        while (lastChild.getNext() != null && lastChild.getParent().getType() == Token.COMMA) {
            lastChild = lastChild.getNext();
        }
        if (n == lastChild) {
            return;
        }
    }
}

if (!isResultUsed && (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler()))) {
    if (isSequenceWithOnlyLastValueUsed) {
        return;  // If inside a sequence, handled by sequence check
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
    
    if (!NodeUtil.isStatement(n)) {
        problemNodes.add(n);
    }
}
