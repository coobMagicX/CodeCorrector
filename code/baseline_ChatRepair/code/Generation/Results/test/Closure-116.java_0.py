private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // Functions called via 'call' and 'apply' have a this-object as the first parameter
  // but this is not part of the called function's parameter list
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
        // Check for arguments that affect mutable state and are used more than once.
        if (NodeUtil.mayEffectMutableState(cArg, compiler)
            && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
          return CanInlineResult.NO;
        }
      } else {
        // If there are remaining parameters without matching arguments, inline is impossible
        return CanInlineResult.NO;
      }

      fnParam = fnParam.getNext();
    }

    if (cArg != null) {
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }

    // It is also necessary to return NO if fnParam still is not null here because it
    // means there are unmatched function parameters left.
    if (fnParam != null) {
      return CanInlineResult.NO;
    }
  }

  return CanInlineResult.YES;
}
