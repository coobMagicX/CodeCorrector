boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Starting from the basic block of the reference, check all parent blocks
    BasicBlock block = ref.getBasicBlock();
    while (block != null) {
        // We need to stop if this block represents a function block boundary
        if (block.isFunctionBlock()) {
            break;
        }
        if (block.isLoop()) {
            return false;
        }
        block = block.getParent();
    }

    return true;
}
