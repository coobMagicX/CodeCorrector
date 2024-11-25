boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Make sure this assignment is not in a loop.
    for (BasicBlock block = ref.getBasicBlock(); 
         block != null && !block.isLoop; 
         block = block.getParent()) {
        if (block.isFunction) {
            break;
        } else if (block.isLoop) {
            return false;
        }
    }

    // Check if the assignment is within a loop, but not in multiple loops
    BasicBlock lastBlock = block;
    while (lastBlock != null && lastBlock.isLoop) {
        lastBlock = lastBlock.getParent();
    }
    for (BasicBlock nestedBlock = ref.getBasicBlock(); 
         nestedBlock != null && !nestedBlock.equals(lastBlock); 
         nestedBlock = nestedBlock.getParent()) {
        if (nestedBlock.isLoop) {
            return false;
        }
    }

    return true;
}