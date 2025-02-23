private void removeUnreferencedFunctionArgs(Scope fnScope, boolean isInAdvancedMode) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // Parameters for object literal setters/getters should not be removed.
    return;
  }

  Node argList = NodeUtil.getFunctionParameters(function);
  if (!isInAdvancedMode) {
    // In simple mode, we leave all parameters intact
    return;
  }
  
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  if (!modifyCallers) {
    // Strip unreferenced args off the end of the function declaration in advanced mode only.
    Node lastArg;
    while ((lastArg = argList.getLastChild()) != null) {
      Var var = fnScope.getVar(lastArg.getString());
      if (var != null && !referenced.contains(var)) {
        argList.removeChild(lastArg);
        compiler.reportCodeChange();
      } else {
        break;
      }
    }
  } else {
    // When allowed, optimize the call sites in advanced mode.
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}
