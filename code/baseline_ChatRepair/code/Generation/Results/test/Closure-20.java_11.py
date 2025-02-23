private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
      callTarget.getString().equals("String")) {
    Node firstArg = callTarget.getNext();
    // Ensure there is exactly one argument to the String function
    if (firstArg != null && firstArg.getNext() == null) {
      // Apply transformation only if the argument is a simple, immutable literal
      if (isSimpleImmutableLiteral(firstArg)) {
        Node addition = IR.add(
            IR.string("").srcref(callTarget),
            firstArg.detachFromParent());
        n.getParent().replaceChild(n, addition);
        reportCodeChange();
        return addition;
      }
    }
  }
  return n;
}

private boolean isSimpleImmutableLiteral(Node node) {
  // Check types that are clearly immutable and without side effects
  return node.isString() || node.isNumber() || node.isNull() || node.isVoid() ||
         node.isName() && ("undefined".equals(node.getString()) || "NaN".equals(node.getString()));
}
