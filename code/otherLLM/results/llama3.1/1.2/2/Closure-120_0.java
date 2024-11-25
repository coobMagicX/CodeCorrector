boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  for (BasicBlock block = ref.getBasicBlock(); 
       block != null && !block.isLoop; 
       block = block.getParent()) {
    if (block.isFunction) {
      break;
    }
  }

  return !block.isLoop;
}