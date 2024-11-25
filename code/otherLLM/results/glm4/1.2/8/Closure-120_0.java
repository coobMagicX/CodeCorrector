boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Make sure this assignment is not in a loop or recursive call.
    Set<BasicBlock> visitedBlocks = new HashSet<>();
    while (ref != null && ref.getBasicBlock() != null) {
        BasicBlock block = ref.getBasicBlock();
        
        if (visitedBlocks.contains(block)) {
            // If we revisit the same block, it's likely a loop or recursion.
            return false;
        }
        visitedBlocks.add(block);

        if (block.isFunction) {
            break;
        } else if (block.isLoop) {
            return false;
        }

        ref = getOneAndOnlyAssignment(); // Get the next reference for validation
    }

    // After the loop, we check if there are no more references assigned to the same variable.
    return !getOneAndOnlyAssignment().equals(ref);
}