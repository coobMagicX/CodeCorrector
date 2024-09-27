private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // Handle cast nodes by finding the first non-cast node
  while (first.isCast()) {
    first = first.getFirstChild();
  }

  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  } else {
    // If the call is a computed property access on a new object,
    // ensure the context is preserved.
    if (first.isGetProp() && first.getFirstChild().isNew()) {
      n.removeProp(Node.FREE_CALL); // Ensure it's not marked as a free call improperly
    }
  }

  // Keep track of the context in which eval is called. It is important
  // to distinguish between "(0, eval)()" and "eval()".
  if (first.isName() && "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}