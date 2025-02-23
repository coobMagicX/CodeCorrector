private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() && callTarget.getString().equals("String")) {
    Node value = callTarget.getNext();
    // Only optimize when there is exactly one argument to the String function
    if (value != null && value.getNext() == null) {
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          value.detach());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  return n;
}
