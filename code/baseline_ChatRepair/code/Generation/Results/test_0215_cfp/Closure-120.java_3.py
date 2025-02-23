boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  // Make sure this assignment is not in a loop.
  for (BasicBlock block = ref.getBasicBlock();
       block != null; block = block.getParent()) {
    // Explicitly checking if the block type is function or loop.
    if (block.isFunction()) {
      break; // If it is a function, scope check is complete, exit loop.
    } else if (block.isLoop()) {
      return false; // Assignment within loop is not once in a lifetime.
    }
    // Added: Handling other block types to ensure complete block hierarchy is checked.
    else if (!block.isFunction() && !block.isLoop() && block.getParent() == null) {
      // If it's neither function nor loop and has no parent, it's a top-level orphan block.
      // Decision