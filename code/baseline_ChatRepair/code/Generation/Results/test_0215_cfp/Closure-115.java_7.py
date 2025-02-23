private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // Analyze the body of the function for potential side effects.
  if (NodeUtil.mayHaveSideEffects(block, compiler)) {
    return CanInlineResult.NO;
  }

  // CALL NODE: [ CALL, NAME|PROPERTY, ARG1, ARG2, ... ]
  Node callee = callNode.getFirstChild();
  Node cArg = callee.getNext();

  // Functions called via 'call' and 'apply' have a 'this' context as the first argument.
  if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
    if (cArg == null) {
      return CanInlineResult.NO;
    }
    cArg = (NodeUtil.isFunctionObjectCall(callNode) && cArg.isThis()) ? cArg.getNext() : cArg;
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();

  // Iterate over each actual parameter and corresponding formal parameter.
  while (cArg != null || fnParam != null) {
    if (cArg != null) {
      // Side-effect checks on actual arguments
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }

    if (fnParam != null) {
      if (cArg != null) {
        // There's a corresponding argument for this parameter
        if (NodeUtil.canBeSideEffected(cArg) && NodeUtil.isImmutableValue(fnParam)) {
          return CanInlineResult.NO;
        }
      }
      fnParam = fnParam.getNext();
    }
  }

  return CanInlineResult.YES;
}
