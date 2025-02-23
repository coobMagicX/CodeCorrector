boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Starting from the basic block of the reference, check all parent blocks
  BasicBlock block = ref.getBasicBlock();
  while (block != null) {
    if (block.isLoop) {
      return false;
    }

    // If function is reached and it's the function containing the assignment, stop checking further.
    if (block.isFunction && block.contains(ref)) {
      break;
    }

    block = block.getParent();
  }

  return true;
}
