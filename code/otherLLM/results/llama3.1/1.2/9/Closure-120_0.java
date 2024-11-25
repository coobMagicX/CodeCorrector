boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    // If no assignment found, consider the variable not assigned.
    return false;
  }

  // Make sure this assignment is not in a loop.
  BasicBlock block = ref.getBasicBlock();
  while (block != null && !block.isFunction) {
    if (block.isLoop) {
      return false; // If assignment is within a loop, return false
    }
    block = block.getParent(); // Move to the parent basic block
  }

  return true;
}