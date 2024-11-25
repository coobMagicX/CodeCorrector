private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // ignore cast nodes and get nodes if they are not part of the call expression.
  if (first != null && !NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);

    // Keep track of the context in which eval is called. It is important
    // to distinguish between "(0, eval)()" and "eval()".
    if (first.isName() &&
        "eval".equals(first.getString())) {
      first.putBooleanProp(Node.DIRECT_EVAL, true);
    }
  }

  // If it's a method call with multiple children, we need to check the second child
  // for direct evaluations of 'eval'.
  if (n.numChildren() > 1 && NodeUtil.isName(n.getChild(1))) {
    Node second = n.getChild(1);
    if ("eval".equals(second.getString())) {
      second.putBooleanProp(Node.DIRECT_EVAL, true);
    }
  }
}