private boolean hasExceptionHandler(Node cfgNode) {
  // Check if the node contains a try-catch block
  if (containsTryCatchBlock(cfgNode)) {
    return true;
  }
  
  return false;
}

private boolean containsTryCatchBlock(Node node) {
  // Traverse the node and its children to check for a try-catch block
  if (node instanceof Try) {
    return true;
  }
  
  for (Node child : node.getChildren()) {
    if (containsTryCatchBlock(child)) {
      return true;
    }
  }
  
  return false;
}