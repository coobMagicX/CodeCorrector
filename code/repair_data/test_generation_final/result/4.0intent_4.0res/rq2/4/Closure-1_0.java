private void removeUnreferencedFunctionArgs(Scope fnScope) {
  // Notice that removing unreferenced function args breaks
  // Function.prototype.length. In advanced mode, we don't really care
  // about this: we consider "length" the equivalent of reflecting on
  // the function's lexical source.
  //
  // Rather than create a new option for this, we assume that if the user
  // is removing globals, then it's OK to remove unused function args.
  //
  // See http://code.google.com/p/closure-compiler/issues/detail?id=253

  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters of object literal setters cannot be removed.
    return;
  }

  // Check for SIMPLE_OPTIMIZATIONS compilation level to avoid removing parameters
  if (compiler.getOptions().getCompilationLevel() == CompilationLevel.SIMPLE_OPTIMIZATIONS) {
    return;  // Do not modify function arguments in SIMPLE_OPTIMIZATIONS mode
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites
      && callSiteOptimizer.canModifyCallers(function);
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