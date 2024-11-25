private boolean hasExceptionHandler(Node cfgNode) {
  try {
    someFunction();
    return false; // No exception occurred, so no exception handler present
  } catch (Exception e) {
    return true; // Exception occurred, so an exception handler is present
  }
}

public ReachingUses apply(List<ReachingUses> from) {
  ReachingUses result = new ReachingUses();
  for (ReachingUses uses : from) {
    result.mayUseMap.putAll(uses.mayUseMap);
  }
  return result;
}