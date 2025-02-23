private CanInlineResult canInlineReferenceDirectly(
    Node callNode, Node fnNode) {
  if (!isDirectCallNodeReplacementPossible(fnNode)) {
    return CanInlineResult.NO;
  }

  Node block = fnNode.getLastChild();

  if (NodeUtil.has(block, new Predicate<Node>() {
    @Override
    public boolean apply(Node n) {
      return (n.isExprResult() && NodeUtil.mayHaveSideEffects(n.getFirstChild(), compiler))
          || n.isReturn() && n.hasChildren() && NodeUtil.mayHaveSideEffects(n.getFirstChild(), compiler);
    }
  }, Predicates.alwaysTrue())) {
    return CanInlineResult.NO;
  }

  // CALL NODE: [ NAME, ARG1, ARG2, ... ]
  Node cArg = callNode.getFirstChild().getNext();

  // Functions called via 'call' and 'apply' have a this-object as
  // the first parameter, but this is not part of the called function's
  // parameter list.
  if (!callNode.getFirstChild().isName()) {
    if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
      // For 'call' and 'apply', bypass the 'this' argument if it's a direct reference.
      if (cArg == null || !cArg.isThis()) {
        return CanInlineResult.NO;
      }
      cArg = cArg.getNext();
    }
  }

  Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
  while (cArg != null && fnParam != null) {
    if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
      return CanInlineResult.NO;
    }
    cArg = cArg.getNext();
    fnParam = fnParam.getNext();
  }

  // Ensure both parameter list and arguments are exhausted.
  if (cArg != null || fnParam != null) {
    return CanInlineResult.NO;
  }

  return CanInlineResult.YES;
}
