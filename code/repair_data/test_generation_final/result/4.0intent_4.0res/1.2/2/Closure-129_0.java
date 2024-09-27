private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // To handle the case where the first child is a cast node,
  // we check for cast and then get the actual first child.
  if (first.isCast()) {
    first = first.getFirstChild();
  }

  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  } else {
    // When the first child is a GETPROP or GETELEM, it indicates
    // an access to a property, possibly on a newly constructed object.
    // This block ensures that such calls are not marked as free calls.
    // Ensure "this" context is properly accounted for in all cases.
    n.putBooleanProp(Node.FREE_CALL, false);
    if (first.isGetProp() && first.getFirstChild().isThis()) {
      n.putBooleanProp(Node.FREE_CALL, false);
    }
  }

  // Keep track of the context in which eval is called. It is important
  // to distinguish between "(0, eval)()" and "eval()".
  if (first.isName() &&
      "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}