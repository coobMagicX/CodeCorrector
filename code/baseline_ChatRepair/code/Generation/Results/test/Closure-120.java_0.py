boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  BasicBlock block = ref.getBasicBlock();
  while (block != null) {
    if (block.isFunction()) {
      // If the block is a function, we can stop verifying further parent blocks.
      return true;  // Assume nested function should effectively shield from outer loops
    } else if (block.isLoop()) {
      // If a loop is encountered, return false.
      return false;
    }
    block = block.getParent();
  }

  return true;
}
