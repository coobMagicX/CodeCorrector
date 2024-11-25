private void removeUnreferencedFunctionArgs(Scope fnScope) {
    Node function = fnScope.getRootNode();

    Preconditions.checkState(function.isFunction());
    if (NodeUtil.isGetOrSetKey(function.getParent())) {
        // The parameters object literal setters can not be removed.
        return;
    }

    Node argList = getFunctionArgList(function);
    boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);

    // Check if we are in "SIMPLE OPTIMIZATIONS" mode before removing unreferenced arguments
    if (isSimpleOptimizationsMode()) {
        // Do not remove any arguments; keep all parameters for simplicity
        return;
    }

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

// Helper method to determine if the current mode is "SIMPLE OPTIMIZATIONS"
private boolean isSimpleOptimizationsMode() {
    // This should return true if the current optimization mode is "SIMPLE OPTIMIZATIONS".
    // The actual implementation would depend on how the compiler's mode state is managed.
    // For example:
    // return simpleOptimizationMode;
}