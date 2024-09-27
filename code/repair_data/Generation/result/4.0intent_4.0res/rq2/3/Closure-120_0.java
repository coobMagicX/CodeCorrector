boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Check if the assignment block is within a loop
    BasicBlock block = ref.getBasicBlock();
    while (block != null) {
        if (block.isFunction()) {
            break;
        } else if (block.isLoop()) {
            return false;
        }
        block = block.getParent();
    }

    return true;
}