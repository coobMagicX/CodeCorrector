private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  
  Node first = n.getFirstChild();

  
  while (first != null && first.isCast()) {
    first = first.getFirstChild();
  }
  
  
  if (first == null) {
    throw new IllegalStateException("Expected a callable item in the call node but got none");
  }

  
  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  
  if (first.isName() && "eval".equals(first.getString())) {
    if (NodeUtil.isInvocationTarget(n)) {
