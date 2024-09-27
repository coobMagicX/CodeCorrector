private boolean hasExceptionHandler(Node cfgNode) {
  return apply(Collections.singletonList(getReachingUses(cfgNode))).mayUseMap.containsKey(exceptionClass);
}