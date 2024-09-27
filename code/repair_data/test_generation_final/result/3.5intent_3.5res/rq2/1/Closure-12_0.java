private boolean hasExceptionHandler(Node cfgNode) {
  ReachingUses reachingUses = new ReachingUses();
  reachingUses = apply(reachingUses);
  
  return reachingUses.mayUseMap.containsKey(cfgNode);
}