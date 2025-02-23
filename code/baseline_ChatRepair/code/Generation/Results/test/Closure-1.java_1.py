private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters in object literal setters cannot be removed.
    return;
  }

  Node argList = NodeUtil.getFunctionParameters(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);
  if (!modifyCallers) {
    // Check if aggressive removal of parameters is enabled
    if (canRemoveParameters()) {  // You will need to implement or ensure this method exists
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

// Example method to determine if parameters can be removed, based on the compiler's configuration
private boolean canRemoveParameters() {
    // You may need to check specific flags or configurations set in your compiler instance or elsewhere.
    // For demonstration, the following is a placeholder and should be adjusted according to your specific context and setup.
    return compiler.getOptimizationLevel() > someThreshold; // Check for some meaningful conditional level
}
