private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
    if (!isDirectCallNodeReplacementPossible(fnNode)) {
        return CanInlineResult.NO;
    }

    Node block = fnNode.getLastChild();

    // CALL NODE: [ NAME, ARG1, ARG2, ... ]
    Node cArg = callNode.getFirstChild().getNext();

    if (!callNode.getFirstChild().isName()) {
        if (NodeUtil.isFunctionObjectCall(callNode)) {
            if (cArg == null || !cArg.isThis()) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        } else if (NodeUtil.isFunctionObjectApply(callNode)) {
            // Prevent inlining if 'apply' is used since the arguments are passed as an array
            // which complicates direct parameter reference counts and effects.
            return CanInlineResult.NO;
        }
    }

    Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
    while (cArg != null || fnParam != null) {
        if (fnParam != null) {
            if (cArg != null) {
                int referenceCount = NodeUtil.getNameReferenceCount(block, fnParam.getString());
                boolean hasSideEffects = NodeUtil.mayHaveSideEffects(cArg, compiler);
                boolean effectsMutableState = NodeUtil.mayEffectMutableState(cArg, compiler);

                if (effectsMutableState && referenceCount > 1) {
                    return CanInlineResult.NO;
                }

                if (hasSideEffects && referenceCount > 0) {
                    return CanInlineResult.NO;
                }
            }
            fnParam = fnParam.getNext();
        }

        if (cArg != null) {
            if (NodeUtil.mayHaveSideEffects(cArg, compiler) || NodeUtil.mayEffectMutableState(cArg, compiler)) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        }
    }

    return CanInlineResult.YES;
}
