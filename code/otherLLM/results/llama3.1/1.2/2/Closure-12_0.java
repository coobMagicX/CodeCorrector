private boolean hasExceptionHandler(Node cfgNode) {
  // Check if the node is an exception handler by looking at its uses in the CFG
  ReachingUses reachingUses = new ReachingUses(new ReachingUses(cfgNode));
  return !reachingUses.mayUseMap.isEmpty();
}