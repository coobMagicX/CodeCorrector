boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  Set<BasicBlock> visitedBlocks = new HashSet<>();
  Queue<BasicBlock> blocksToProcess = new LinkedList<>();
  blocksToProcess.add(ref.getBasicBlock());

  while (!blocksToProcess.isEmpty()) {
    BasicBlock currentBlock = blocksToProcess.poll();
    if (visitedBlocks.contains(currentBlock)) {
      // We've already processed this block, check if it's a loop
      for (BasicBlock successor : currentBlock.getSuccs()) {
        if (successor.isLoop) {
          return false;
        }
      }
      continue;
    }
    visitedBlocks.add(currentBlock);

    // Check if the block is a function or a loop and break if it is
    if (currentBlock.isFunction) {
      break;
    } else if (currentBlock.isLoop) {
      return false;
    }

    // Add all successors to the queue for processing
    for (BasicBlock successor : currentBlock.getSuccs()) {
      blocksToProcess.add(successor);
    }
  }

  return true;
}