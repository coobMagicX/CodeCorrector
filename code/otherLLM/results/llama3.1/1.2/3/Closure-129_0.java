private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of of the "this" context of a call.  A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // ignore cast nodes.

  if (NodeUtil.isCast(first)) {
    // If it's a cast, we should be setting the FREE_CALL property on its parent,
    // not itself. This should fix the issue where FREE_CALL was being set on cast
    // nodes.
    n.getParent().putBooleanProp(Node.FREE_CALL, true);
  } else if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  // Keep track of the context in which eval is called. It is important
  // to distinguish between "(0, eval)()" and "eval()".
  if (first.isName() &&
      "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}