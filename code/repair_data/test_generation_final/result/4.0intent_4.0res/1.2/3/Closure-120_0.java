boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    boolean isAssignedInCurrentScopeOnly = true;
    BasicBlock currentBlock = ref.getBasicBlock();

    // Check if the assignment is within a loop or multiple times in recursive functions.
    while (currentBlock != null) {
        if (currentBlock.isFunction) {
            // Start from the function block and check for re-assignments in the same scope.
            for (BasicBlock block = currentBlock; block != null; block = block.getNext()) {
                if (block == ref.getBasicBlock()) {
                    continue; // Skip the block where the original assignment is made.
                }
                if (block.isLoop) {
                    isAssignedInCurrentScopeOnly = false;
                    break;
                }
                // Check if there's another assignment to the same variable in this function scope.
                if (block.hasAssignment(ref.getNode())) {
                    isAssignedInCurrentScopeOnly = false;
                    break;
                }
            }
            break; // Once the function scope is checked, no need to go further up.
        } else if (currentBlock.isLoop) {
            isAssignedInCurrentScopeOnly = false;
            break;
        }
        currentBlock = currentBlock.getParent();
    }

    return isAssignedInCurrentScopeOnly;
}