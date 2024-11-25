boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  boolean isInLoop = false; // New flag to track if the block is part of a loop
  for (BasicBlock block = ref.getBasicBlock();
       block != null && !isInLoop; block = block.getParent()) {
    if (block.isFunction) {
      break;
    } else if (block.isLoop) {
      isInLoop = true; // Mark the loop flag as true if it's a loop
    }
  }

  return !isInLoop; // Return true only if it is not in a loop
}