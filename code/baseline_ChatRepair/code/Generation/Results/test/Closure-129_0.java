private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  
  
  Node first = n.getFirstChild();

  
  while (first.isCast()) {
    first = first.getFirstChild();
  }

  
  
  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  
  
  if (first.isName() && "eval".equals(first.getString())) {
    n.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}
