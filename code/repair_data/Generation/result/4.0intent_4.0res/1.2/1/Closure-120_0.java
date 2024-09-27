boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Initialize a set to track unique function scopes encountered
    Set<BasicBlock> functionScopes = new HashSet<>();

    // Make sure this assignment is not in a loop or re-assigned in recursive functions.
    for (BasicBlock block = ref.getBasicBlock(); block != null; block = block.getParent()) {
        if (block.isFunction) {
            // Check if we have already encountered this function scope
            if (!functionScopes.add(block)) {
                // If this function scope is already in the set, it means there's a recursive call
                return false;
            }
        } else if (block.isLoop) {
            return false;
        }
    }

    return true;
}