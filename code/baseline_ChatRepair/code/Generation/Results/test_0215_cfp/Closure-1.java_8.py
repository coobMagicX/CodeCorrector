private void removeUnreferencedFunctionArgs(Scope fnScope, boolean isSimpleMode) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());

  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters of object literal setters cannot be removed
    return;
  }

  // Only proceed to remove unreferenced function arguments if not in simple mode
  if (isSimpleMode) {
    return;
  }

  Node argList = NodeUtil.getFunctionParameters(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  if (!modifyCallers) {
    // Strip unreferenced args off the end of the function declaration.
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
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}
