private boolean hasExceptionHandler(Node cfgNode) {
  if (cfgNode == null || !cfgNode.hasType("node")) return false;
  // Check properties of Node.js configuration node to see if it has an exception handler set up
  // For example, if the node has a 'catch' property or if its type is 'exception_handler'
  return cfgNode.hasProperty("catch") || cfgNode.getType().equals("exception_handler");
}