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
  while (cArg != null || fnParam != null) {
    if (fnParam != null) {
      if (cArg != null) {
        if (NodeUtil.mayEffectMutableState(cArg, compiler) && 
            NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
          return CanInlineResult.NO;
        }
        cArg = cArg.getNext();
      } else if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        // If there's no corresponding function parameter,
        // but the call argument may have side effects
        return CanInlineResult.NO;
      }
      fnParam = fnParam.getNext();
    } else {
      if (cArg != null) {
        if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
          return CanInlineResult.NO;
        }
        cArg = cArg.getNext();
      }
    }
  }

  return CanInlineResult.YES;
}
