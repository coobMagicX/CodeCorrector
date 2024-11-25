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
    if (fnParam != null && cArg != null) {
      if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
        return CanInlineResult.NO;
      }
      // Check for arguments that are evaluated more than once.
      if (NodeUtil.mayEffectMutableState(cArg, compiler)
          && NodeUtil.getNameReferenceCount(
              block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }

      // Move to the next name and argument.
      fnParam = fnParam.getNext();
      cArg = cArg.getNext();
    } else if (fnParam != null) {
      // If there are no more arguments but there are still parameters,
      // check for remaining parameters with no matching arguments, which
      // could indicate a problem with the call signature.
      return CanInlineResult.NO;
    }

    // If we reach here and only cArg is not null, it means that there are
    // extra arguments that do not have corresponding named parameters, which
    // should not happen. Check for this and return NO if found.
    if (cArg != null && fnParam == null) {
      return CanInlineResult.NO;
    }
  }

  return CanInlineResult.YES;
}