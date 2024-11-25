if (isFoldableExpressBlock(thenBranch)) {
    Node thenOp = getBlockExpression(thenBranch).getFirstChild();
    if (NodeUtil.isAssignmentOp(thenOp) && 
        areNodesEqualForInlining(thenOp.getFirstChild(), elseOp.getFirstChild()) && 
        !mayEffectMutableState(thenOp)) {

        // ...
    }
}