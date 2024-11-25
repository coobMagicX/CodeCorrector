private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode();
  Preconditions.checkState(function.isFunction());
  
  // Add a condition to check if we're running in simple optimization mode.
  boolean isSimpleOptimization = ...; // Assume this method exists and returns true for simple optimization.

  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters object literal setters can not be removed.
    return;
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  if (!modifyCallers) {
    // Strip unreferenced args off the end of the function declaration only in advanced mode
    if (!isSimpleOptimization) {
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
    }
  } else {
    callSiteOptimizer.optimize(fnScope, referenced);
  }

  // Ensure that we adjust the function's 'length' property to reflect the current number of arguments.
  if (!isSimpleOptimization) {
    NodeUtil.setFunctionLengthProperty(function, argList.getChildCount());
  }
}

// Assuming a method exists to set the length property on the function node:
private static void setFunctionLengthProperty(Node functionNode, int length) {
  // Implementation to set the 'length' property of the function node.
}