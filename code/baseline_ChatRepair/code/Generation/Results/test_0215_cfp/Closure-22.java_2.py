public void visit(NodeTraversal t, Node n, Node parent) {
  if (n.isEmpty() || n.isComma())
    return;

  if (parent == null)
    return;

  boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
  boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());

  if (!isResultUsed) {
    if (NodeUtil.isSideEffectFreeSequenceOrSimpleOp(n, t.getCompiler())) {
      // Constructing more specific messages based on the scenario
      String msg = "This code lacks side-effects. Is there a bug?";
      if (n.isString())
        msg = "Is there a missing '+' on the previous line?";
      else if (isSimpleOp)
        msg = "The result of the '" + Token.name(n.getType()).toLowerCase() + "' operator is not being used.";

      t.getCompiler().report(t.makeError(n, DiagnosticGroups.CHECK_SIDE_EFFECTS, msg));
      if (!NodeUtil.isStatement(n))
        problemNodes.add(n);
    }
  }
}

/** 
 * Utility class updated methods (included or updated in NodeUtil class).
 */
public static boolean isSideEffectFreeSequenceOrSimpleOp(Node n, AbstractCompiler compiler) {
  if (isSimpleOperatorType(n.getType()))
      return true;

  if (n.isComma()) {
    Node c = n.getFirstChild();
    while (c != null) {
      if (mayHaveSideEffects(c, compiler))
        return false;
      c = c.getNext();
    }
    return true;
  }

  return mayHaveSideEffects(n, compiler);
}
