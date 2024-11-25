boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  BasicBlock block = ref.getBasicBlock();
  while (block != null) {
    if (block.isFunction()) {
      break;
    } else if (block.isLoop()) {
      return false;
    }
    block = block.getParent();
  }

  return true;
}