boolean isAssignedOnceInLifetime() {
  Reference ref = getOneAndOnlyAssignment();
  if (ref == null) {
    return false;
  }

  boolean inLoop = false;
  Set<BasicBlock> visitedBlocks = new HashSet<>();
  
  // Helper function to traverse the control flow graph recursively.
  void traverse(BasicBlock block) {
    if (!visitedBlocks.add(block)) {
      inLoop = true;
      return;
    }
    
    for (Instruction instruction : block.getInstructions()) {
      if (instruction instanceof Assignment) {
        // Check if this assignment is the one we are interested in.
        if (ref == ((Assignment) instruction).getLeftOperand().getReference()) {
          return; // We've found our assignment, no need to continue.
        }
      }
    }

    for (BasicBlock child : block.getChildren()) {
      traverse(child);
    }
  }
  
  BasicBlock startBlock = ref.getBasicBlock();
  if (startBlock != null) {
    traverse(startBlock);
  }
  
  // If we found a loop, return false.
  return !inLoop;
}