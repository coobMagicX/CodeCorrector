boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  Set<BasicBlock> visitedBlocks = new HashSet<>();
  
  // Make sure this assignment is not in a loop.
  BasicBlock currentBlock = ref.getBasicBlock();
  while (currentBlock != null && !visitedBlocks.contains(currentBlock)) {
    visitedBlocks.add(currentBlock);
    
    if (currentBlock.isFunction) {
      break;
    } else if (currentBlock.isLoop) {
      return false;
    }
    
    // Traverse to parent block
    currentBlock = currentBlock.getParent();
  }

  return true;
}