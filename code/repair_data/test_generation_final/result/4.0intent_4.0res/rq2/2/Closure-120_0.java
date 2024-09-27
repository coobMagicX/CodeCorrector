boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Make sure this assignment is not in a loop.
    for (BasicBlock block = ref.getBasicBlock(); block != null; block = block.getParent()) {
        if (block.isFunction()) { // Corrected from `block.isFunction` to `block.isFunction()`
            break;
        } else if (block.isLoop()) { // Corrected from `block.isLoop` to `block.isLoop()`
            return false;
        }
    }

    return true;
}