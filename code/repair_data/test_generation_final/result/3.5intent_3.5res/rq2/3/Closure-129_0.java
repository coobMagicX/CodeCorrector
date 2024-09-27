private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call.  A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // Ignore cast nodes.
  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  } else {
    n.removeProp(Node.FREE_CALL); // Remove the FREE_CALL property if it exists
  }

  // Keep track of the context in which eval is called. It is important
  // to distinguish between "(0, eval)()" and "eval()".
  if (first.isName() && "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  } else {
    first.removeProp(Node.DIRECT_EVAL); // Remove the DIRECT_EVAL property if it exists
  }
}