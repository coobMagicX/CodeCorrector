private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // Analyze the function block for side effects.
  boolean hasSideEffects = NodeUtil.mayHaveSideEffects(block, compiler);

  // CALL NODE: [ NAME, ARG1, ARG2, ... ]
  Node cArg = callNode.getFirstChild().getNext();

  // Adjust for "call" or "apply"
  if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
    if (cArg == null || (!cArg.isThis() && !callNode.getFirstChild().isName())) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();
  }

  // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    if (fnParam != null && cArg != null) {
      if (NodeUtil.canBeSideEffected(cArg) && hasSideEffects) {
        return CanInlineResult.NO;
      }
      if (NodeUtil.mayEffectMutableState(cArg, compiler)
          && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
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
  }
  
  return CanInlineResult.YES;
}
