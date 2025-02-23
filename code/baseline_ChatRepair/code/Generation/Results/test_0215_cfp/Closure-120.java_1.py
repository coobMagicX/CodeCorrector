boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  BasicBlock block = ref.getBasicBlock();
  while (block != null) {
    if (block.isFunctionBlock()) {  // Check if the proper method or property name is used here.
      break;
    } else if (block.isLoopBlock()) {  // Check if this is the correct method or property to detect loop blocks.
      return false;
    }
    block