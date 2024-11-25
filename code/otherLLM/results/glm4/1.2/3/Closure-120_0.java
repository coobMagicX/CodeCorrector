boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  Set<BasicBlock> visitedBlocks = new HashSet<>();
  BasicBlock currentBlock = ref.getBasicBlock();

  while (currentBlock != null && !visitedBlocks.contains(currentBlock)) {
    visitedBlocks.add(currentBlock);

    // Check for recursion in loops
    boolean isLoop = currentBlock.isLoop();
    if (isLoop) {
      return false;
    }

    // Move up the call stack, looking for a function or reaching the end of the code
    while (!currentBlock.isFunction && !visitedBlocks.contains(currentBlock)) {
      BasicBlock parentBlock = currentBlock.getParent();

      if (parentBlock == null || visitedBlocks.contains(parentBlock)) {
        break;
      }
      currentBlock = parentBlock;
    }

    // If we're in a function, break out of the loop
    if (currentBlock.isFunction) {
      break;
    }

    // If we reached here without breaking, it means we are in an infinite loop
    if (!visitedBlocks.contains(currentBlock)) {
      return false;
    }
  }

  // If we are still in a function after visiting all blocks, this assignment is not once-in-lifetime
  if (currentBlock.isFunction) {
    return false;
  }

  return true;
}