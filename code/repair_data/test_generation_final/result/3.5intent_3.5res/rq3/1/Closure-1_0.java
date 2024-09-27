private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters object literal setters can not be removed.
    return;
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites
      && callSiteOptimizer.canModifyCallers(function);
  if (!modifyCallers) {
    // Strip unreferenced args off the end of the function declaration.
    Node lastArg;
    while ((lastArg = argList.getLastChild()) != null) {
      String argName = lastArg.getString();
      Var var = fnScope.getVar(argName);
      if (!referenced.contains(var)) {
        argList.removeChild(lastArg);
        fnScope.undeclare(var);
        compiler.reportCodeChange();
      } else {
        break;
      }
    }
  } else {
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}