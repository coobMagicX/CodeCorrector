private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
    if (!isDirectCallNodeReplacementPossible(fnNode)) {
        return CanInlineResult.NO;
    }

    Node block = fnNode.getLastChild();

    // CALL NODE: [ NAME, ARG1, ARG2, ... ]
    Node cArg = callNode.getFirstChild().getNext();

    // Functions called via 'call' and 'apply' have special considerations:
    if (!callNode.getFirstChild().isName()) {
        if (NodeUtil.isFunctionObjectCall(callNode) || NodeUtil.isFunctionObjectApply(callNode)) {
            // Skip the 'this' argument for .call()
            if (NodeUtil.isFunctionObjectCall(callNode) && cArg != null && cArg.isThis()) {
                cArg = cArg.getNext();
            } 
            // Return NO for .apply() as it should be handled differently due to arguments being an array.
            else if (NodeUtil.isFunctionObjectApply(callNode)) {
                return CanInlineResult.NO;
            }
            else {
                // Unexpected case or no proper 'this' arg when required.
                return CanInlineResult.NO;
            }
        }
    }

    // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
    Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
    while (cArg != null || fnParam != null) {
        // Sync up the parameters and arguments:
        if (fnParam != null) {
            if (cArg != null) {
                // Check for arguments that may affect mutable state,
                // or are evaluated more than once
                if (NodeUtil.mayEffectMutableState(cArg, compiler) && 
                    NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
                    return CanInlineResult.NO;
                }
                cArg = cArg.getNext();
            }
            fnParam = fnParam.getNext();
        } else if (cArg != null) {
            // If there are more arguments than parameters and they may cause side effects
            if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        }
    }

    return CanInlineResult.YES;
}
