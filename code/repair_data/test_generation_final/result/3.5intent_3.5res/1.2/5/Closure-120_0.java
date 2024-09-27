boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop or recursive function.
  for (BasicBlock block = ref.getBasicBlock();
       block != null; block = block.getParent()) {
    if (block.isFunction) {
      // Check if the function is recursive
      if (block.getParent() != null && block.getParent().isFunction) {
        return false;
      }
      break;
    } else if (block.isLoop) {
      return false;
    } else if (block.isRecursiveFunction) {
      return false;
    }
  }

  return true;
}