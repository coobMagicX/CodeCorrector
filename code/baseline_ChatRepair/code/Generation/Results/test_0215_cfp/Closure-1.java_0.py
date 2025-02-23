private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters in object literal setters can not be removed.
    return;
  }

  Node argList = NodeUtil.getFunctionParameters(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);
  if (!modifyCallers) {
    // Let's check if we're in a simple optimization mode that does care about 
    // preserving function's "length" property in non-advanced mode.
    if (!compiler.getOptions().shouldPreserveFunctionArguments()) {
      // Strip unreferenced args off the end of the function declaration if not preserving arguments.
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
    }
  } else {
    // This path handles changes across callsites and arguments together
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}
