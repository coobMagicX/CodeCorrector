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
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          value.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  } else if (callTarget != null && callTarget.isName() &&
             (callTarget.getString().equals("String") || callTarget.getString().equals("Array"))) {
    // Fold String(a) or Array(a) to '' + (a) or [a] on immutable literals
    Node value = callTarget.getNext();
    if (value != null) {
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          value.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  } else {
    // Call to String or Array with more than one argument
    // Don't fold it for now. This is a limitation of this simple folding logic.
    // More complex handling of function calls should be considered in the future.
    return n;
  }
  return n;
}