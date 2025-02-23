private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  // Check if the function body may have side effects more thoroughly.
  boolean hasSideEffects = NodeUtil.functionMayHaveSideEffects(fnNode, compiler);

  // Consider all children of the function body block.
  if (block.hasChildren()) {
    for (Node child = block.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.mayHaveSideEffects(child, compiler)) {
        hasSideEffects = true;
        break;
      }
    }
  }

  Node firstArg = callNode.getFirstChild().getNext();

  // Handle special cases with 'call' and 'apply'.
  if (callNode.getFirstChild().isGetProp()) {
    Node callTarget = callNode.getFirstChild();
    if (callTarget.getString().equals("call") || callTarget.getString().equals("apply")) {
      if (firstArg != null && firstArg.isThis()) {
        firstArg = firstArg.getNext(); // Skip the 'this' argument for 'call' and 'apply'.
      } else {
        return CanInlineResult.NO; // Missing expected 'this' argument.
      }
    }
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  Node cArg = firstArg;
  while (fnParam != null || cArg != null) {
    if (fnParam != null && cArg != null) {
      if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
        return CanInlineResult.NO;
      }
      if (NodeUtil.mayEffectMutableState(cArg, compiler)
          && NodeUtil.getNameReferenceCount(
              block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else if (cArg != null) { // Arguments without matching parameters, check for side effects.
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }
    fnParam = fnParam != null ? fnParam.getNext() : null;
  }

  return CanInlineResult.YES;
}
