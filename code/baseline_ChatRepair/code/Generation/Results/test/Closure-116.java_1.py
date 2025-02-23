private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // CALL NODE: [ NAME, ARG1, ARG2, ... ]
  Node cArg = callNode.getFirstChild().getNext();

  // Handling 'call' and 'apply'
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode)) {
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else if (NodeUtil.isFunctionObjectApply(callNode)) {
      // '.apply' calls are not inlinable if they include non-trivial arguments
      if (cArg != null && !NodeUtil.isEmptyBlock(cArg)) {
        return CanInlineResult.NO;
      }
    } else {
      // this is an error state, should never reach here since all '.apply' should be caught
      Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
    }
  }

  // FUNCTION NODE -> LP (Parameter List) NODE: [ ARG1, ARG2, ... ]
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    if (fnParam != null) {
      // If there is a function parameter, check if there is a corresponding argument
      if (cArg != null) {
        // Check for arguments that are evaluated more than once.
        if (NodeUtil.mayEffectMutableState(cArg, compiler) && 
            NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
          return CanInlineResult.NO;
        }
      } else {
        // If there is a function parameter but no corresponding argument, return NO
        return CanInlineResult.NO;
      }
      fnParam = fnParam.getNext();
    }
    
    if (cArg != null) {
      // Check all arguments for side-effects.
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }

    // If there are still function parameters left after running out of arguments
    if (fnParam != null) {
        return CanInlineResult.NO;
    }
  }

  return CanInlineResult.YES;
}
