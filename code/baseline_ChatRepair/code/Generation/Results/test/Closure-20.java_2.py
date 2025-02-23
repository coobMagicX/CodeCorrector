private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  
  if (callTarget != null && callTarget.isName() &&
      callTarget.getString().equals("String")) {
    Node firstArg = callTarget.getNext();
    // Ensure there is exactly one argument to the String function.
    if (firstArg != null && firstArg.getNext() == null) {
      // Fold String(a) to '' + (a) on immutable literals,
      // only when there is exactly one argument.
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          firstArg.cloneTree());
      n.getParent().replaceChild(n, addition);
      // Ensure the argument node is removed from the original parent.
      callTarget.removeChild(firstArg);
      reportCodeChange();
      return addition;
    }
  }
  return n;
}
