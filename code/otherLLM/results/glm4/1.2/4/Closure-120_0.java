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

    if (currentBlock.isFunction()) {
      return false; // Function call indicates we are not in the original scope
    } else if (currentBlock.isLoop()) {
      return false;
    }

    currentBlock = currentBlock.getParent();
  }

  return true;
}