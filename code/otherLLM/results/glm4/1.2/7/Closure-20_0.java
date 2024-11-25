private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Check the next sibling to see if it's a simple literal value
    Node value = callTarget.getNext();
    if (value != null) {
      // Ensure that the value is a simple literal and not a complex expression
      if (value.isImmutableLiteral()) {
        Node addition = IR.add(
            IR.string("").srcref(callTarget),
            value.detachFromParent());
        n.getParent().replaceChild(n, addition);
        reportCodeChange();
        return addition;
      }
    }
  }
  return n;
}