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
      Var var = fnScope.getVar(lastArg.getString());
      if (referenced.contains(var)) {
        break; // If referenced, stop removing arguments
      }
      if (!NodeUtil.isFunctionDeclaration(lastArg)) { // Ignore function declarations
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