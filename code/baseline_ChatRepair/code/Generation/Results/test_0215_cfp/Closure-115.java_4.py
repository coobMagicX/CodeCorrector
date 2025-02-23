private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
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
  
  // Adjust for `call` and `apply` method handling
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode)) {
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else if (NodeUtil.isFunctionObjectApply(callNode)) {
      if (cArg == null || !NodeUtil.isArrayLit(cArg)) {
        return CanInlineResult.NO;
      }
      // Cannot inline when using `.apply` with an array that might have side effects
      return CanInlineResult.NO;
    } else {
      return CanInlineResult.NO;
    }
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (fnParam != null && cArg != null) {
    if (NodeUtil.canBeSideEffected(cArg)) {
      if (hasSideEffects || NodeUtil.mayEffectMutableState(cArg, compiler)
          || NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
    }

    if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
      return CanInlineResult.NO;
    }

    fnParam = fnParam.getNext();
    cArg = cArg.getNext();
  }

  // Check remaining call arguments for side effects in case of excess arguments.
  while (cArg != null) {
    if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();
  }

  return CanInlineResult.YES;
}
