private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // Check for side effects in the function body.
  boolean hasSideEffects = false;
  for (Node child = block.getFirstChild(); child != null; child = child.getNext()) {
    if (NodeUtil.mayHaveSideEffects(child, compiler)) {
      hasSideEffects = true;
      break;
    }
  }

  // CALL NODE: [ NAME, ARG1, ARG2, ... ]
  Node cArg = callNode.getFirstChild().getNext();

  // Special cases: Functions called via 'call' and 'apply' have a this-object as the first parameter
  if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
    if (cArg == null || !cArg.isThis()) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();  // Skip the 'this' argument for 'call' and 'apply'.
  }

  // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    if (fnParam != null) {
      if (cArg != null) {
        if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
          return CanInlineResult.NO;
        }
        // Check arguments that are evaluated more than once
        if (NodeUtil.mayEffectMutableState(cArg, compiler)
            && NodeUtil.getNameReferenceCount(
                block, fnParam.getString()) > 1) {
          return CanInlineResult.NO;
        }
        cArg = cArg.getNext();
      }
      fnParam = fnParam.getNext();
    } else {
      // For every call argument check for side-effects, even if there isn't a named parameter to match
      if (cArg != null) {
        if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
          return CanInlineResult.NO;
        }
        cArg = cArg.getNext();
      }
    }
  }

  return CanInlineResult.YES;
}
