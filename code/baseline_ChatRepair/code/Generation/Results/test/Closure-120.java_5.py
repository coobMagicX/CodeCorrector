boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false; // If there's no assignment, it's false.
  }

  // Ensure this assignment is not in a loop.
  for (BasicBlock block = ref.getBasicBlock(); block != null; block = block.getParent()) {
    if (block.isFunctionBlock()) {
      break; // Exit the loop when the enclosing function block is found.
    } else if (block.isLoopBlock()) {
      return false; // The variable is assigned inside a loop.
    }
  }

  return true; // Assignment is once in a lifetime if not within a loop.
}
