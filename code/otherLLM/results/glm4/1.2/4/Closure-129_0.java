private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an explicit
  // "this" is a free call.
  Node first = n.getFirstChild();

  // ignore cast nodes.
  if (first != null && !NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  // Keep track of the context in which eval is called. It is important to
  // distinguish between "(0, eval)()" and "eval()".
  if (first != null && first.isName() &&
      "eval".equals(first.getString())) {
    // Only set direct evaluation if it's a call to 'eval' without any other nodes,
    // which would indicate a more complex context.
    Node second = first.getNextSibling();
    n.putBooleanProp(Node.DIRECT_EVAL, (second == null || !second.isCall()));
  }
}