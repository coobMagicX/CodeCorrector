private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
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

  // CALL NODE: [ NAME, ARG1, ARG2, ... ]
  Node cArg = callNode.getFirstChild().getNext();

  // Functions called via 'call' and 'apply' have a this-object as
  // the first parameter, but this is not part of the called function's
  // parameter list.
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode)) {
      // Support replace this with a value.
      if (cArg == null || !cArg.isThis() || cArg.getNext() != null) { // Check for 'this' and no additional arguments
        return CanInlineResult.NO;
      }
    } else {
      // ".apply" call should be filtered before this.
      Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
    }
  }

  if (hasSideEffects && !callMeetsBlockInliningRequirements(t, callNode, fnNode, Collections.emptySet())) {
    return CanInlineResult.NO;
  }

  Set<String> namesToAlias = new HashSet<>();
  CallSiteType callSiteType = classifyCallSite(callNode);
  if (callSiteType == CallSiteType.UNSUPPORTED) {
    return CanInlineResult.NO;
  }

  if (!allowDecomposition
      && (callSiteType == CallSiteType.DECOMPOSABLE_EXPRESSION
          || callSiteType == CallSiteType.EXPRESSION)) {
    return CanInlineResult.NO;
  }

  return canInlineReferenceAsStatementBlock(t, callNode, fnNode, namesToAlias);
}