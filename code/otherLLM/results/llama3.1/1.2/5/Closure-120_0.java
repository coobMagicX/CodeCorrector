boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  while (true) {
    BasicBlock block = ref.getBasicBlock();
    if (block.isFunction || !block.isLoop) break;
    else if (!block.isLoop) break;
    ref = getOneAndOnlyAssignmentFromLoop(block);
  }

  return true;
}

Reference getOneAndOnlyAssignmentFromLoop(BasicBlock loopBlock) {
  // This method should be implemented to find the one and only assignment 
  // within a given loop block. The implementation details are not provided.
}