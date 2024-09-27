private void annotateCalls(Node n) {
    Preconditions.checkState(n.isCall());

    // Keep track of the "this" context of a call. A call without an
    // explicit "this" is a free call.
    Node first = n.getFirstChild();

    // Remove cast nodes to ensure we are checking the actual function call node.
    while (first.isCast()) {
        first = first.getFirstChild();
    }

    if (!NodeUtil.isGet(first)) {
        n.putBooleanProp(Node.FREE_CALL, true);
    }

    // Keep track of the context in which eval is called. It is important
    // to distinguish between "(0, eval)()" and "eval()".
    if (first.isName() &&
        "eval".equals(first.getString())) {
        // Check if the eval call is indirect or direct
        if (n.getSecondChild() != null && n.getSecondChild().isName() && "eval".equals(n.getSecondChild().getString())) {
            n.putBooleanProp(Node.DIRECT_EVAL, true);
        } else {
            n.putBooleanProp(Node.DIRECT_EVAL, false);
        }
    }
}