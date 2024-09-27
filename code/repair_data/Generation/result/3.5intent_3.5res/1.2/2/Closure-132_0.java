private Node tryMinimizeIf(Node n) {

  Node parent = n.getParent();

  Node cond = n.getFirstChild();

  if (NodeUtil.isLiteralValue(cond, true)) {
    return n;
  }

  Node thenBranch = cond.getNext();
  Node elseBranch = thenBranch.getNext();

  if (elseBranch == null) {
    if (isFoldableExpressBlock(thenBranch)) {
    // code as before
  } else {
    if (NodeUtil.isStatementBlock(thenBranch) &&
        thenBranch.hasOneChild()) {
      // code as before
    }
  }

  tryRemoveRepeatedStatements(n);

  if (cond.isNot() && !consumesDanglingElse(elseBranch)) {
    // code as before
  }

  if (isReturnExpressBlock(thenBranch) && isReturnExpressBlock(elseBranch)) {
    // code as before
  }

  boolean thenBranchIsExpressionBlock = isFoldableExpressBlock(thenBranch);
  boolean elseBranchIsExpressionBlock = isFoldableExpressBlock(elseBranch);

  if (thenBranchIsExpressionBlock && elseBranchIsExpressionBlock) {
    // code as before
  }

  boolean thenBranchIsVar = isVarBlock(thenBranch);
  boolean elseBranchIsVar = isVarBlock(elseBranch);

  if (thenBranchIsVar && elseBranchIsExpressionBlock &&
      getBlockExpression(elseBranch).getFirstChild().isAssign()) {
    // code as before
  } else if (elseBranchIsVar && thenBranchIsExpressionBlock &&
      getBlockExpression(thenBranch).getFirstChild().isAssign()) {
    // code as before
  }

  return n;
}