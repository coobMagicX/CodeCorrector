boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  for (BasicBlock block = ref.getBasicBlock();
       block != null; block = block.getParent()) {
    if (block.isFunction) {
      break;
    } else if (hasLoop(block)) { // Utilize new method: hasLoop
      return false;
    }
  }

  return true;
}

// New method to detect loops in a BasicBlock
boolean hasLoop(BasicBlock block) {
  while (block != null && !block.isFunction) {
    if (block.isLoop) {
      return true;
    }
    block = block.getParent();
  }
  return false; // If no loop is found, return false
}