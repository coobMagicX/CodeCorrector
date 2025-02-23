private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // Parameters for object literal setters/getters should not be removed.
    return;
  }

  Node argList = NodeUtil.getFunctionParameters(function);

  // Assuming `modifyCallSites` reflects the current mode's aggressiveness about altering call sites.
  if (!modifyCallSites) {
      // In simple mode or when not altering call sites, do not modify function arguments.
      return;
  } else {
    boolean modifyCallers = callSiteOptimizer.canModifyCallers(function);
    if (!modifyCallers) {
      // Strip unreferenced args off the end of the function declaration only if it's safe to modify call sites.
      Node lastArg;
      while ((lastArg = argList.getLastChild()) != null) {
        Var var = fnScope.getVar(lastArg.getString());
        if (var != null && !referenced.contains(var)) {
          argList.removeChild(lastArg);
          compiler.reportCodeChange();
        } else {
          break; // Stop when a referenced argument is encountered
        }
      }
    } else {
      // When allowed, optimize the call sites to match updated function parameters.
      callSiteOptimizer.optimize(fnScope, referenced);
    }
  }
}
