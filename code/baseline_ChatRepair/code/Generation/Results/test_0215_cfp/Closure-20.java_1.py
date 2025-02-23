private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  
  if (callTarget != null && callTarget.isName() &&
    callTarget.getString().equals("String")) {
    // Check that only one argument is provided to the String function
    Node firstArg = callTarget.getNext();
    if (firstArg != null && firstArg.getNext() == null) {
      // Fold String(a) to '' + (a) on immutable literals,
      // which allows further optimizations
      //
      // We can't do this in the general case, because String(a) has
      // slightly different semantics than '' + (a). See
      // http://code.google.com/p/closure-compiler/issues/detail?id=759
      Node value = firstArg;
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
