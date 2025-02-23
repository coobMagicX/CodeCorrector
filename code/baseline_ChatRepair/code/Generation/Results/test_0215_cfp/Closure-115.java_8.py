private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  // Determine if the function block (the last child) has side effects.
  Node block = fnNode.getLastChild();
  if (NodeUtil.mayHaveSideEffects(block, compiler)) {
    return CanInlineResult.NO; // Fail early if the function block itself may have side effects.
  }

  // Process the function call node to get its arguments.
  Node callee = callNode.getFirstChild();
  Node cArg = callee.getNext();

  // Adjust for function calls that use 'call' or 'apply' that would affect 'this' context positioning.
  if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
    if (cArg == null || !cArg.isThis()) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext(); // skip the 'this' argument for call or apply.
  }

  // Get the first parameter of the function.
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();

  // Align argument checks with parameter list.
  while (cArg != null || fnParam != null) {
    // If there is a corresponding parameter for each argument, check for side effects.
    if (fnParam != null && cArg != null) {
      if (NodeUtil.canBeSideEffected(cArg) && NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      if (NodeUtil.mayEffectMutableState(cArg, compiler) && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
      fnParam = fnParam.getNext();
      cArg = cArg.getNext();
    } else {
      // Either an argument or a parameter ran out before the other, indicating mismatch.
      if (fnParam != null || cArg != null) {
        return CanInlineResult.NO; // Prevent inlining if mismatch found.
      }
    }
  }

  return CanInlineResult.YES; // If all checks pass, then inlining is possible.
}
