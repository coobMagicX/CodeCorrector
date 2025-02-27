private void removeUnreferencedFunctionArgs(Scope fnScope) {
  // Notice that removing unreferenced function args breaks Function.prototype.length.
  // In advanced mode, we don't really care about this: we consider "length" the equivalent of reflecting on
  // the function's lexical source.
  //
  // Rather than create a new option for this, we assume that if the user
  // is removing globals, then it's OK to remove unused function args.
  //
  // See http://code.google.com/p/closure-compiler/issues/detail?id=253

  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters object literal setters can not be removed.
    return;
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

  // Check the compilation level before attempting to remove unreferenced arguments
  if (modifyCallers) {
    switch (compiler.getOptimizationLevel()) {
      case SIMPLE:
        // Do not remove unused parameters when using SIMPLE optimization level
        break;
      default:
        callSiteOptimizer.optimize(fnScope, referenced);
        break;
    }
  } else {
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
}

// Ensure the compiler and related methods are defined elsewhere in your codebase