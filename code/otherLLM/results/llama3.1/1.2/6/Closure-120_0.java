boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  BasicBlock block = ref.getBasicBlock();
  while (block != null && !block.isFunction) {
    block = block.getParent();
    if (block.isLoop) {
      return false;
    }
  }

  return true;
}