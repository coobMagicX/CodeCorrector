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
      // TODO(johnlenz): Support replace this with a value.
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else {
      // ".apply" call should be filtered before this.
      Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
    }
  }

  // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    // For each named parameter check if a mutable argument use more than one.
    if (fnParam != null) {
      if (cArg != null) {
        // Check for arguments that are evaluated more than once, handling multiple instances
        int argCount = 0;
        while (cArg != null && cArg.isName()) {
          String argName = cArg.getString();
          if (NodeUtil.getNameReferenceCount(block, argName) > 1) {
            return CanInlineResult.NO; // More than one mutable reference found
          }
          cArg = cArg.getNext();
          argCount++;
        }

        if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
          return CanInlineResult.NO;
        }
        
        if (argCount == 0) { // Handle case where no named parameters match
          if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
            return CanInlineResult.NO; // Side effects found but no matching parameter
          }
        } else {
          // Check for arguments that are evaluated more than once.
          if (NodeUtil.mayEffectMutableState(cArg, compiler)
              && NodeUtil.getNameReferenceCount(
                  block, fnParam.getString()) > 1) {
            return CanInlineResult.NO;
          }
        }

      }

      // Move to the next name.
      fnParam = fnParam.getNext();
    }

    // For every call argument check for side-effects, even if there
    // isn't a named parameter to match.
    if (cArg != null) {
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }
  }

  return CanInlineResult.YES;
}