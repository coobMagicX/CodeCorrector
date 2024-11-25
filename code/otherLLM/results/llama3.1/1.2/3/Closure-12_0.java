private boolean hasExceptionHandler(Node cfgNode) {
  MaybeReachingVariableUse maybeReach = new MaybeReachingVariableUse(getGraph(), getScope(), this);
  ReachingUses uses = maybeReach.apply(Collections.singletonList(new ReachingUses()));
  return uses.mayUseMap.containsKey("exceptionHandler");
}