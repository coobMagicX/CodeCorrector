private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call.  A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // ignore cast nodes, which seem to be a special case in the annotation process.
  if (NodeUtil.isCast(first)) {
    return; // Ignore cast nodes
  }

  // Keep track of of the context in which eval is called. It is important
  // to distinguish between "(0, eval)()" and "eval()".
  Node second = first.getNextSibling();
  if (second != null && second.isName() &&
      "eval".equals(second.getString())) {
    n.putBooleanProp(Node.DIRECT_EVAL, true);
  }

  // Check if the call is a free call
  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }
}