private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();
  Node cArg = callNode.getFirstChild().getNext();

  // CALLBACKs via 'call' and 'apply' have a this-object as the first parameter; we start considering from the next.
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode)) {
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext(); // move to actual first argument
    } else {
      Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
    }
  }

  // Get the function's formal parameters
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();

  // Walk through each argument and each function parameter
  while (cArg != null || fnParam != null) {
    if (fnParam != null) {
      // Check if parameter reference exists in function body
      int refCount = NodeUtil.getNameReferenceCount(block, fnParam.getString());
      
      if (cArg != null) {
        // Check for arguments that may affect mutable state and are referenced multiple times
        if (NodeUtil.mayEffectMutableState(cArg, compiler) && refCount > 1) {
          return CanInlineResult.NO;
        }
        cArg = cArg.getNext();
      }
      fnParam = fnParam.getNext();
    } else if (cArg != null) {
      // If there are more call arguments than function parameters, check they do not have side-effects
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }
  }

  return CanInlineResult.YES;
}
