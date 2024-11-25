private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        (callTarget.getString().equals("String") || callTarget.getString().equals("Array"))) {
    // Fold String(a), Array([a]), or other primitive type constructors
    // to '' + (a) on immutable literals, which allows further optimizations
    //
    // We can't do this in the general case, because String(a) has slightly different semantics than '' + (a).
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
             callTarget.getString().equals("String") && 
             value != null && value.getNumberOfChildren() == 1) {
        // Handle function calls with single argument
        Node arg = value.getFirstChild();
        if (arg != null) {
          Node addition = IR.add(
              IR.string("").srcref(callTarget),
              arg.detachFromParent());
          n.getParent().replaceChild(n, addition);
          reportCodeChange();
          return addition;
        }
  }
  return n;
}