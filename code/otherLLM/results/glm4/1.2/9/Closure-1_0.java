private void removeUnreferencedFunctionArgs(Scope fnScope) {
    Node function = fnScope.getRootNode();
    Preconditions.checkState(function.isFunction());
    
    if (NodeUtil.isGetOrSetKey(function.getParent())) {
        // The parameters object literal setters can not be removed.
        return;
    }

    Node argList = getFunctionArgList(function);
    boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

    if (!modifyCallers) {
        // We need to ensure that we do not remove arguments that are referenced elsewhere.
        Set<Var> allReferencedArgs = new HashSet<>();
        for (Node arg : argList.getChildren()) {
            Var var = fnScope.getVar(arg.getString());
            if (referenced.contains(var)) {
                allReferencedArgs.add(var);
            }
        }

        // Now remove the unreferenced arguments from the end.
        Node lastArg;
        while ((lastArg = argList.getLastChild()) != null) {
            Var var = fnScope.getVar(lastArg.getString());
            if (!allReferencedArgs.contains(var)) {
                argList.removeChild(lastArg);
                compiler.reportCodeChange();
            } else {
                break; // Stop when we reach a referenced argument.
            }
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}