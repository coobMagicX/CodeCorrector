boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false; // If there's no assignment, it's false.
  }

  // Make sure this assignment is not in a loop.
  for (BasicBlock block = ref.getBasicBlock(); block != null; block = block.getParent()) {
    if (block.isFunction()) {
      break; // Exit loop when function block is reached since it's out of loop scope.
    } else if (block.isLoop()) {
      return false; // Return false if assignment is in a loop.
    }
  }

  return true; // Return true if it passes all checks: assigned once and not in a loop.
}
