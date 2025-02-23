private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
    if (!isDirectCallNodeReplacementPossible(fnNode)) {
        return CanInlineResult.NO;
    }

    Node block = fnNode.getLastChild();

    // CALL NODE: [ NAME, ARG1, ARG2, ... ]
    Node cArg = callNode.getFirstChild().getNext();

    // Handling 'call' and 'apply'
    if (!callNode.getFirstChild().isName()) {
        if (NodeUtil.isFunctionObjectCall(callNode)) {
            if (cArg == null || !cArg.isThis()) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        } else if (NodeUtil.isFunctionObjectApply(callNode)) {
            // '.apply' calls are always non-inlineable currently (simplified assumption)
            return CanInlineResult.NO;
        } else {
            throw new IllegalStateException("Unexpected function call structure");
        }
    }

    // FUNCTION NODE -> LP (Parameter List) NODE: [ ARG1, ARG2, ... ]
    Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();

    while (cArg != null || fnParam != null) {
        if (fnParam != null) {
            if (cArg != null) {
                // Check for multiple calls to a single argument resulting in multiple effects which
                // can lead to incorrect results if inlined
                if (NodeUtil.mayEffectMutableState(cArg, compiler) &&
                    NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
                    return CanInlineResult.NO;
                }
                cArg = cArg.getNext();
            } else {
                // Parameter exists without corresponding argument
                return CanInlineResult.NO;
            }
            fnParam = fnParam.getNext();
        } else {
            // There are more arguments than parameters
            if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        }
    }

    return CanInlineResult.YES;
}
