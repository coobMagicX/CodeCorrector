private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of of the "this" context of a call. A call without an
  // explicit "this" is considered a free call.
  Node first = n.getFirstChild();

  // If the first child is not a GET node, it's not referencing 'this',
  // hence it could be a free call. We assume that all other kinds of nodes
  // are not free calls as per the given logic.
  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  } else {
    // If the first child is a GET node, we need to check for a specific case
    // where it represents 'this' and hence not a free call. The repair here
    // includes an additional condition to handle such cases.
    if (first.getString() != null && !first.getString().equals("this")) {
      n.putBooleanProp(Node.FREE_CALL, true);
    }
  }

  // Keep track of the context in which 'eval' is called. It's important to
  // distinguish between "(0, eval)()" and "eval()". We check if the first child
  // is a NAME node and represents the string "eval".
  if (first.isName() &&
      "eval".equals(first.getString())) {
    // To avoid confusion with direct evaluation of 'new Function()', we must
    // ensure that it's not followed by another function call. This additional
    // check helps in correcting potential issues.
    Node second = first.getNextSibling();
    if (second != null && !second.isCall()) {
      first.putBooleanProp(Node.DIRECT_EVAL, true);
    }
  }
}