private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

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
      // Check for arguments that are evaluated more than once.
      if (NodeUtil.mayEffectMutableState(cArg, compiler)) {
        int refCount = NodeUtil.getNameReferenceCount(block, fnParam.getString());
        // If the reference count is greater than 1 and not in a loop, it's non-inlinable.
        boolean isInLoop = false;
        for (Node node : NodeUtil.getNodesInBlock(block, node -> {
          if (node.isSameNode(fnParam)) {
            isInLoop = true;
            return true; // Stop the iteration
          }
          return false;
        })) {
          if (isInLoop) break;
        }

        if (refCount > 1 && !isInLoop) {
          return CanInlineResult.NO;
        }
      }

      fnParam = fnParam.getNext();
    } else if (cArg != null) {
      // For every call argument check for side-effects, even if there
      // isn't a named parameter to match.
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else {
      // If both cArg and fnParam are null, we've checked all arguments without
      // finding a non-inlinable condition.
      break;
    }
  }

  return CanInlineResult.YES;
}