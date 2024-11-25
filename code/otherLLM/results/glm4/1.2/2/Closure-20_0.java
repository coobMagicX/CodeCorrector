private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Check if there are any additional arguments to the String constructor
    Node value = callTarget.getNext();
    int childCount = n.getChildCount();

    // If the callTarget has more than one child, it means there are additional arguments,
    // so we don't optimize this call.
    if (value != null && childCount == 2) {
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          value.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  // If no optimization is done or if the call is not applicable,
  // return the original node unchanged.
  return n;
}