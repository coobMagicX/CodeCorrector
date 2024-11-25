private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters object literal setters can not be removed.
    return;
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  if (!modifyCallers) {
    // Strip unreferenced args off the end of the function declaration.
    for (int i = argList.getChildCount() - 1; i >= 0; i--) {
      Node argNode = argList.getChildAt(i);
      Var var = fnScope.getVar(argNode.getString());
      if (!referenced.contains(var)) {
        argList.removeChild(argNode);
        compiler.reportCodeChange();
      } else {
        // If an unreferenced argument is found, do not remove further arguments.
        break;
      }
    }
  } else {
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}