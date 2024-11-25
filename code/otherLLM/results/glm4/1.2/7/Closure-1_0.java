private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters object literal setters can not be removed.
    return;
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  List<Node> argsToRemove = new ArrayList<>();
  if (!modifyCallers) {
    // Collect unreferenced args to remove, but do not actually remove them yet.
    for (Node arg : argList.getChildren()) {
      Var var = fnScope.getVar(arg.getString());
      if (!referenced.contains(var)) {
        argsToRemove.add(arg);
      } else {
        break; // Stop when a referenced argument is found
      }
    }

    // Now remove the unreferenced arguments from the arg list.
    for (Node arg : argsToRemove) {
      argList.removeChild(arg);
      compiler.reportCodeChange();
    }
  } else {
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}