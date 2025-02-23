private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
  // Check if direct call node replacement is possible
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();
  Node firstCallArg = callNode.getFirstChild().getNext();

  // Handles special cases for functions called via 'call' or 'apply'
  Node cArg = firstCallArg;
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
      // In function calls via `call` or `apply`, first argument is the context (`this`)
      if (cArg != null && cArg.isThis()) {
        cArg = cArg.getNext(); // skip the 'this' argument
      } else {
        // If this is missing or not a 'this', inlining is not possible
        return CanInlineResult.NO;
      }
    }
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null || fnParam != null) {
    // If we have more arguments or parameters to check
    if (cArg != null && fnParam != null) {
      // Check arguments matching with parameters
      if (NodeUtil.mayEffectMutableState(cArg, compiler) && 
          NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
      fnParam = fnParam.getNext();
    } else if (cArg != null) {
      // Inlining is not possible if there are more arguments than parameters
      // and those additional arguments may have side effects
      if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    } else {
      // Break the loop if there are no more call arguments,
      // remaining function parameters don't require arguments to match them
      break;
    }
  }

  return CanInlineResult.YES;
}
