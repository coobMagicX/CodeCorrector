private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();
  Node cArg = callNode.getFirstChild().getNext();

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
  boolean hasSeenSideEffects = false;

  while (cArg != null || fnParam != null) {
    if (cArg != null && NodeUtil.mayHaveSideEffects(cArg, compiler)) {
      hasSeenSideEffects = true;
    }

    if (fnParam != null) {
      int refCount = NodeUtil.getNameReferenceCount(block, fnParam.getString());
      if (cArg != null && NodeUtil.mayEffectMutableState(cArg, compiler) && refCount > 1) {
        return CanInlineResult.NO;
      }
      fnParam = fnParam.getNext();
    }

    if (cArg != null) {
      cArg = cArg.getNext();
    }
  }

  if (hasSeenSideEffects) {
    return CanInlineResult.NO;
  }

  return CanInlineResult.YES;
}
