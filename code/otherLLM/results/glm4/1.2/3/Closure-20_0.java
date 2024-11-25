private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Fold String(a) to '' + (a) on immutable literals,
    // which allows further optimizations
    //
    // We can't do this in the general case, because String(a) has
    // slightly different semantics than '' + (a). See
    // http://code.google.com/p/closure-compiler/issues/detail?id=759

    Node value = callTarget.getNext();
    if (value != null) {
      // Check for the second argument to see if it is an invocation of 'new'
      if (!value.isCall() || !value.getFirstChild().isName() ||
          !value.getFirstChild().getString().equals("String")) {
        // If not, do not simplify and return the original node
        return n;
      }

      Node secondArg = value.getNext();
      if (secondArg != null) {
        // Do not simplify if there is a third argument or more after 'new String'
        return n;
      }

      // It's a single-argument call to 'String' constructor, so we can fold it
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          value.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  // If it's not a call to the 'String' constructor or other conditions are not met,
  // do not simplify and return the original node
  return n;
}