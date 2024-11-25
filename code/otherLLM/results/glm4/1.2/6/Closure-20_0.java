private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Check for additional parameters after the first argument
    Node value = callTarget.getNext();
    while (value != null && !value.isEmpty()) {
      value = value.getNext();
    }

    // If there are no more nodes, we have a simple function call to String with one argument
    if (value == null) {
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          n.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  return n;
}