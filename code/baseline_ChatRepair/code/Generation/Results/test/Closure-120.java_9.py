boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  for (BasicBlock block = ref.getBasicBlock();
       block != null; block = block.getParent()) {
    if (block.isLoop()) {
      return false;
    } else if (block.isFunction()) {
      // Breaking out of the loop if it encounters function type block
      // after confirming no enclosing loops have been found.
      break;
    }
  }

  return true;
}
