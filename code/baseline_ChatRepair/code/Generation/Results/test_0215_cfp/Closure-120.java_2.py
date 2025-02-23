boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Make sure this assignment is not in a loop.
    BasicBlock block = ref.getBasicBlock();
    while (block != null) {
        if (block.isFunction()) {  // Assuming a correctly named method exists for function block check.
            break;
        } else if (block.isLoop()) {  // Assuming a correctly named method exists for loop block check.
            return false;
        }
        block = block.getParent();
    }

    return true;
}
