private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        (callTarget.getString().equals("String") ||
         callTarget.getString().equals("Array"))) {
    // Fold String(a) or Array(b) to '' + (a) or [b] on immutable literals,
    // which allows further optimizations
    //
    // We can't do this in the general case, because String(a) has
    // slightly different semantics than '' + (a). See
    // http://code.google.com/p/closure-compiler/issues/detail?id=759
    Node value = callTarget.getNext();
    if (value != null) {
      Node addition;
      if (callTarget.getString().equals("String")) {
        addition = IR.add(
            IR.string("").srcref(callTarget),
            value.detachFromParent());
      } else {
        addition = IR.array(value.detachFromParent()).srcref(callTarget);
      }
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    } else if (value == null && callTarget.getString().equals("String")) {
      // Handle cases like String('hello', bar()); by folding the first argument
      Node folded = IR.add(
          IR.string("").srcref(callTarget),
          value.detachFromParent());
      n.getParent().replaceChild(n, folded);
      reportCodeChange();
      return folded;
    }
  }
  return n;
}