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
    // For each named parameter check if a mutable argument use more than one.
    if (fnParam != null && cArg != null) {
      if (NodeUtil.mayEffectMutableState(cArg, compiler)
          && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
    }

    // Move to the next name or argument.
    if (fnParam != null) {
      fnParam = fnParam.getNext();
    } else if (cArg != null) {
      cArg = cArg.getNext();
    }
  }

  return CanInlineResult.YES;
}