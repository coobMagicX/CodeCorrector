private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an explicit "this" is a free call.
  Node first = n.getFirstChild();

  // Ignore cast nodes and new expressions.
  if (!NodeUtil.isGet(first) && !first.isNew()) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  // Keep track of the context in which eval is called. Distinguish between "(0, eval)()" and "eval()".
  if (first.isName() && "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}