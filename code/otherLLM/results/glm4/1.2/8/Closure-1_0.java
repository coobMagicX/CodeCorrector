private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters object literal setters can not be removed.
    return;
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  // Reverse iterate over the arguments to avoid changing the indices of the subsequent arguments
  for (int i = argList.getChildCount() - 1; i >= 0; i--) {
    Node currentArg = argList.getChildAt(i);
    Var var = fnScope.getVar(currentArg.getString());
    if (!referenced.contains(var)) {
      argList.removeChild(currentArg);
      compiler.reportCodeChange();
      // Break the loop once an unreferenced argument is removed
      break;
    }
  }

  if (modifyCallers) {
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}