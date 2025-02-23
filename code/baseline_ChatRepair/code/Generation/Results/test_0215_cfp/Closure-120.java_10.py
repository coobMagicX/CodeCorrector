boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  for (BasicBlock block = ref.getBasicBlock();
       block != null; block = block.getParent()) {
    if (block.isLoop) { // Assume `isLoop` is a property and not a method.
      return false;
    }
    if (block.isFunction) { // Assume `isFunction` is a property and not a method.
      break;
    }
  }

  return true;
}
