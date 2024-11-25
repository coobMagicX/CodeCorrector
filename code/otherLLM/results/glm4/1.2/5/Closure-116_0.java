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
  Map<String, Integer> nameReferenceCount = new HashMap<>();
  
  while (cArg != null || fnParam != null) {
    if (fnParam != null && cArg != null) {
      // Check for arguments that are evaluated more than once.
      String paramName = fnParam.getString();
      if (NodeUtil.mayEffectMutableState(cArg, compiler)
          && nameReferenceCount.merge(paramName, 1, Integer::sum) > 1) {
        return CanInlineResult.NO;
      }
    }

    // For every call argument check for side-effects, even if there
    // isn't a named parameter to match.
    if (cArg != null && NodeUtil.mayHaveSideEffects(cArg, compiler)) {
      return CanInlineResult.NO;
    }

    // Move to the next name and argument.
    if (fnParam != null) {
      fnParam = fnParam.getNext();
    }
    if (cArg != null) {
      cArg = cArg.getNext();
    }
  }

  return CanInlineResult.YES;
}