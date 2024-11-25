private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // ignore cast nodes.

  if (!NodeUtil.isGet(first)) {
    // Only mark as free call if there is no 'this' node or it's not a property access on 'this'
    boolean hasThis = (first != null) && NodeUtil.isThis(first);
    if (!hasThis || !NodeUtil.isGetOfThis(first, "abc")) {
      n.putBooleanProp(Node.FREE_CALL, true);
    }
  }

  // Keep track of the context in which eval is called. It is important
  // to distinguish between "(0, eval)()" and "eval()".
  if (first.isName() &&
      "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}