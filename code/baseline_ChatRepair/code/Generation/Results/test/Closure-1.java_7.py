private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());

  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters of object literal setters cannot be removed
    return;
  }
  
  // Update this condition with the correct method to check if the compiler is in simple mode
  if (!compiler.getOptions().removeUnusedVariables) {
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
