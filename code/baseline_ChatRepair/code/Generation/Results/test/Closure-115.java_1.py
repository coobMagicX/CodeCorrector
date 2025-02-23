private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  // This check was modified to consider all children of the function block for side effects.
  Node block = fnNode.getLastChild();
  if (NodeUtil.mayHaveSideEffects(block, compiler)) {
    return CanInlineResult.NO;
  }

  // Start argument handling after function name
  Node cArg = callNode.getFirstChild().getNext();

  // Bypass the 'this' keyword if it exists in uses of .call or .apply.
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (fnParam != null || cArg != null) {
    if (fnParam != null && cArg != null) {
      if (NodeUtil.canBeSideEffected(cArg)) {
        return CanInlineResult.NO;
      }
      // Check for arguments that are evaluated more than once.
      if (NodeUtil.mayEffectMutableState(cArg, compiler) && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
    }

    // Progress to the next parameter and argument.
    if (fnParam != null) {
      fnParam = fnParam.getNext();
    }
    if (cArg != null) {
      cArg = cArg.getNext();
    }

    // Ensure both lists exhausted at the same rate; otherwise we likely have a mismatch.
    if ((cArg == null && fnParam != null) || (cArg != null && fnParam == null)) {
      return CanInlineResult.NO;
    }
  }

  return CanInlineResult.YES;
}
