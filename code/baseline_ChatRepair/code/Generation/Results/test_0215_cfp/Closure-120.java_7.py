boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Starting from the basic block of the reference, check all parent blocks until a function barrier or null.
    BasicBlock block = ref.getBasicBlock();
    while (block != null && !block.isFunction()) {
        if (block.isLoop()) {
            return false;
        }
        block = block.getParent();
    }

    return true;
}
