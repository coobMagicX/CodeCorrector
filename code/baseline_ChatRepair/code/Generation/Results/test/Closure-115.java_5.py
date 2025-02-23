private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  boolean hasSideEffects = false;
  if (block.hasChildren()) {
    Preconditions.checkState(block.hasOneChild());
    Node stmt = block.getFirstChild();
    if (stmt.isReturn()) {
      hasSideEffects = NodeUtil.mayHaveSideEffects(stmt.getFirstChild(), compiler);
    }
  }

  Node cArg = callNode.getFirstChild().getNext();

  // 'call' and 'apply' have a 'this' object as first parameter
  // Ignore `this` for function object calls
  if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
    if (cArg == null || !cArg.isThis()) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (fnParam != null && cArg != null) {
    if (hasSideEffects && NodeUtil.canBeSideEffected(cArg, compiler)) {
      return CanInlineResult.NO;
    }

    // Check if a parameter is referenced more than once and the argument has side effects
    if (NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1 && 
        NodeUtil.mayEffectMutableState(cArg, compiler)) {
      return CanInlineResult.NO;
    }

    cArg = cArg.getNext();
    fnParam = fnParam.getNext();
  }

  // Check any additional arguments for side effects if there are more arguments than parameters.
  while (cArg != null) {
    if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();
  }

  return CanInlineResult.YES;
}
