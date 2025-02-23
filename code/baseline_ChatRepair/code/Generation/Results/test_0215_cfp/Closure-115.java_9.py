private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // Check if any statement in the function body may have side-effects.
  boolean hasSideEffects = NodeUtil.mayHaveSideEffects(block, compiler);

  // CALL NODE: [ NAME, ARG1, ARG2, ... ]
  Node cArg = callNode.getFirstChild().getNext();

  // Functions called via 'call' and 'apply' have a this-object as
  // the first parameter, but this is not part of the called function's
  // parameter list.
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

  // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    if (fnParam != null) {
      if (cArg != null) {
        if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
          return CanInlineResult.NO;
        }
        if (NodeUtil.mayEffectMutableState(cArg, compiler)
            && NodeUtil.getNameReferenceCount(
                block, fnParam.getString()) > 1) {
          return CanInlineResult.NO;
        }
      }
      fnParam = fnParam.getNext();
    }
    if (cArg != null) {
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }
  }

  return CanInlineResult.YES;
}
