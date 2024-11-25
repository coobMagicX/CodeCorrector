boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }

    // Use a HashSet to track visited blocks to detect cycles
    Set<BasicBlock> visitedBlocks = new HashSet<>();
    Deque<BasicBlock> stack = new ArrayDeque<>();
    
    stack.push(ref.getBasicBlock());

    while (!stack.isEmpty()) {
        BasicBlock currentBlock = stack.pop();
        
        if (visitedBlocks.contains(currentBlock)) {
            // A cycle is detected, so return false
            return false;
        }
        
        visitedBlocks.add(currentBlock);
        
        if (currentBlock.isFunction || currentBlock.isLoop) {
            // If the block is a function or loop, we should stop checking further as it's considered an assignment
            break;
        }

        // Add all children blocks to stack for further processing
        for (BasicBlock child : currentBlock.getChildren()) {
            if (!visitedBlocks.contains(child)) {
                stack.push(child);
            }
        }
    }

    return true;
}