private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  
  
  Node first = n.getFirstChild();

  
  while (first != null && first.isCast()) {
    first = first.getFirstChild();
  }

  
  if (first == null) {
    throw new IllegalStateException("Call node does not have a recognizable target after ignoring casts.");
  }

  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  
  
  if (first.is