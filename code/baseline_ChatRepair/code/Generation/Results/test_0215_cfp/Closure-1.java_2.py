private void removeUnreferencedFunctionArgs(Scope fnScope) {
    Node function = fnScope.getRootNode();

    Preconditions.checkState(function.isFunction());
    if (NodeUtil.isGetOrSetKey(function.getParent())) {
        // The parameters in object literal setters can not be removed.
        return;
    }

    Node argList = NodeUtil.getFunctionParameters(function);
    boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);
    // Access customFlags or some equivalent method/field to check configuration
    if (!modifyCallers && customFlags.allowArgumentRemoval()) {
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
    } else if (modifyCallers) {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}

// Example hypothetical check method; You might need to adapt its implementation
// to actually consult whatever configuration or flags are available in your environment:
private boolean allowArgumentRemoval() {
    // Assuming a method or a flag exists to check whether argument removal is allowed:
    return compiler.getConfig().isArgumentRemovalEnabled();
}
