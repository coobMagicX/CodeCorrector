private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  boolean hasSideEffects = false;
  if (block.hasChildren()) {
    Preconditions.checkState(block.hasOneChild());
    Node stmt = block.getFirstChild();
    if (stmt.isReturn()) {
      hasSideEffects = NodeUtil.mayHaveSideEffects(stmt.getFirstChild(), compiler);
    }
  }

  Node cArg = callNode.getFirstChild().getNext();

  // Handle special cases for 'call' and 'apply'
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode)) {
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else {
      Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
    }
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    if (fnParam != null && cArg != null) {
      // Check for side effects and mutable state effects
      if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
        return CanInlineResult.NO;
      }
      if (NodeUtil.mayEffectMutableState(cArg, compiler)
          && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
    }

    // Move to the next argument or parameter
    if (fnParam != null) {
      fnParam = fnParam.getNext();
    } else if (cArg != null) {
      cArg = cArg.getNext();
    }
  }

  return CanInlineResult.YES;
}