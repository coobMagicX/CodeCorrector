private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();
  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // Do not remove parameters for setters/getters
    return;
  }
  
  // Check if modification of the function's caller sites is allowed or not
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  Node argList = getFunctionArgList(function);

  if (!modifyCallers) {
    // Only proceed to remove parameters if the mode allows for parameter removal
    Node lastArg;
    while ((lastArg = argList.getLastChild()) != null) {
      Var var = fnScope.getVar(lastArg.getString());
      if (!referenced.contains(var)) {
        argList.removeChild(lastArg);
        compiler.reportCodeChange();
      } else {
        // Once an used argument is found, stop removing
        break;
      }
    }
  } else {
    // In case where callers can be modified, delegate optimization to callSiteOptimizer
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}
