if (n.isExprResult()) {
    Node child = n.getFirstChild();
    // When checking side-effects, consider each component of a sequence expression.
    // This change ensures we consider each expression in a sequence (mostly used with commas).
    if (child.isComma()) {
        Node sequenceChild = child;
        while (sequenceChild != null) {
            if (NodeUtil.mayHaveSideEffects(sequenceChild, t.getCompiler())) {
                return; // There are side-effects, no warning needed.
            }
            sequenceChild = sequenceChild.getNext();
        }
    }
}

if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
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
