private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();
  
  Preconditions.checkState(function.isFunction());
  
  // Check for simple mode and skip parameter removal if in simple mode
  // assuming `isSimpleMode` is the flag that indicates if the compiler is in simple mode
  if (isSimpleMode) {
    return;
  }
  
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // Parameters of object literal setters cannot be removed
    return;
  }

  Node argList = NodeUtil.getFunctionParameters(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);
  
  if (!modifyCallers) {
    // Strip unreferenced args off the end of the function declaration.
    Node lastArg;
    while ((lastArg = argList.getLastChild()) != null) {
      Var var = fnScope.getVar(lastArg.getString());
      if (!referenced.contains(var)) {
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
