private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // CALL NODE: [ NAME, ARG1, ARG2, ... ]
  Node cArg = callNode.getFirstChild().getNext();

  // Functions called via 'call' and 'apply' have a this-object as
  // the first parameter, but this is not part of the called function's
  // parameter list.
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode)) {
      // TODO(johnlenz): Support replace this with a value.
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else {
      // ".apply" call should be filtered before this.
      Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
    }
  }

  // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    if (fnParam != null && cArg != null) {
      // Check for arguments that are evaluated more than once.
      if (NodeUtil.mayEffectMutableState(cArg, compiler)) {
        int refCount = NodeUtil.getNameReferenceCount(block, fnParam.getString());
        if (refCount > 1) {
          return CanInlineResult.NO;
        }
      }

      // Move to the next argument and parameter.
      cArg = cArg.getNext();
      fnParam = fnParam.getNext();
    } else if (fnParam != null) {
      // Move to the next parameter only if there is no current argument.
      fnParam = fnParam.getNext();
    } else if (cArg != null) {
      // Check for side effects of the current argument if it's the last one.
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
    }

    // No need to do anything when both cArg and fnParam are null as we have processed all arguments and parameters.
  }

  return CanInlineResult.YES;
}