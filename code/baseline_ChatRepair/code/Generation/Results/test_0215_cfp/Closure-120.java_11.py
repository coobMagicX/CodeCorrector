boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  BasicBlock block = ref.getBasicBlock();
  while (block != null) {
    if (block.isLoop) {
      return false;
    }
    block = block.getParent();
  }

  // After exiting the loop, check if the root block is indeed a function block
  // This ensures that we've traced back to the function without encountering a loop
  return block != null && block.isFunction;
}
