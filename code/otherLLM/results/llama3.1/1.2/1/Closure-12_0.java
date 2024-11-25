private boolean hasExceptionHandler(Node cfgNode) {
  ReachingUses reachingUses = createEntryLattice();
  computeMayUse(cfgNode, cfgNode, reachingUses, false);
  return !reachingUses.mayUseMap.containsKey("x");
}