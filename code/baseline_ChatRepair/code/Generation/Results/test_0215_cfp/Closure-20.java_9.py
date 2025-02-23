private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
      callTarget.getString().equals("String")) {
    Node firstArg = callTarget.getNext();
    // Check if there is exactly one argument to the String function
    if (firstArg != null && firstArg.getNext() == null) { 
      // Only apply optimization if there is one argument
      Node addition = IR.add(
          IR.string("").srcref(callTarget), 
          firstArg.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  return n;
}
