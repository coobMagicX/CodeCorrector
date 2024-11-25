private boolean hasExceptionHandler(Node cfgNode) {
  if (cfgNode instanceof TryCatchBlock) {
    return true;
  }
  
  // Check for children nodes if the node is a composite node.
  for (Node child : ((CompositeNode) cfgNode).getChildren()) {
    if (hasExceptionHandler(child)) { // Recursive call to check each child
      return true;
    }
  }
  
  return false; // No exception handling constructs found
}