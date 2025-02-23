private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();
  Node cArg = callNode.getFirstChild().getNext();

  // Skip the first argument for 'call' as it is the 'this' keyword object.
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
    if (fnParam != null && cArg != null) {
      if (NodeUtil.mayEffectMutableState(cArg, compiler) && 
          NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
      fnParam = fnParam.getNext();
      cArg = cArg.getNext();
    } else if (fnParam == null && cArg != null) {
      // If there are still call arguments left while parameter list is exhausted, check for side effects.
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else {
      // Break out of the loop when both cArg and fnParam are null
      // or when cArg runs out but fnParam still has remaining parameters(unmatched parameters are not a concern for inlining decision if there are no more arguments).
      break;
    }
  }

  return CanInlineResult.YES;
}
