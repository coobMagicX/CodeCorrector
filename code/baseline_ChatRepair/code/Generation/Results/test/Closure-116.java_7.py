private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();
  Node cArg = callNode.getFirstChild().getNext();

  // Skip the first argument if it's a CALL or APPLY method on functions which use 'this'
  if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
    if (cArg == null || !cArg.isThis()) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();

  while (cArg != null || fnParam != null) {
    if (cArg != null) {
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }

    if (fnParam != null) {
      if (cArg != null && NodeUtil.mayEffectMutableState(cArg, compiler)) {
        int refCount = NodeUtil.getNameReferenceCount(block, fnParam.getString());
        if (refCount > 1) {
          return CanInlineResult.NO;
        }
      }
      fnParam = fnParam.getNext();
    }
  }

  // If there are more arguments than parameters and any of those extra arguments may have side effects
if (cArg != null) {
  do {
    if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();
  } while (cArg != null);
}

  return CanInlineResult.YES;
}
