boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  Set<BasicBlock> visitedBlocks = new HashSet<>();
  Stack<BasicBlock> stackToProcess = new Stack<>();
  BasicBlock currentBlock = ref.getBasicBlock();

  // Add the starting block to the stack
  stackToProcess.push(currentBlock);

  while (!stackToProcess.isEmpty()) {
    currentBlock = stackToProcess.pop();
    
    if (visitedBlocks.contains(currentBlock)) {
      // If we encounter a block that has already been visited, there is a loop.
      return false;
    }
    visitedBlocks.add(currentBlock);

    // Process the children of the current block
    for (BasicBlock child : currentBlock.getChildren()) {
      stackToProcess.push(child);
    }

    // If the current block is not the entry point and it's not part of a function, continue up the chain.
    if (!currentBlock.isFunction && !visitedBlocks.contains(currentBlock.getParent())) {
      BasicBlock parent = currentBlock.getParent();
      if (parent != null) {
        stackToProcess.push(parent);
      }
    }

    // If we've reached a function or the end of the chain, break out of the loop check.
    if (currentBlock.isFunction || visitedBlocks.contains(currentBlock.getParent())) {
      break;
    }
  }

  return true;
}