boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  boolean inLoop = false;
  for (BasicBlock block = ref.getBasicBlock();
       block != null; block = block.getParent()) {
    if (block.isFunction) {
      break;
    } else if (block.isLoop) {
      inLoop = true;
      break;
    }
  }

  // If we exited a loop, return false
  if (!inLoop && ref.getBasicBlock().isExitFromLoop()) {
    return false;
  }

  return true;
}