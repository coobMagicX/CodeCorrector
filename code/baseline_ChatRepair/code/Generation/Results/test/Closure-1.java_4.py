private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();
  Preconditions.checkState(function.isFunction());

  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters in object literal setters cannot be removed.
    return;
  }

  // Check if the compiler is set to preserve arguments or is in a mode that is not aggressive
  // This might depend on the actual compiler setup and might need a correct API call
  if (!compiler.isAggressiveChange()) {
    return; // Do not modify anything if not in aggressive mode.
  }

  Node argList = getFunctionArgList(function);
  if (!modifyCallSites || !callSiteOptimizer.canModifyCallers(function)) {
    // Strip unreferenced args off the end of the function declaration if not modifying call sites.
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
