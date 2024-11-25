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
      Node[] children = n.getChildren(); // Get all children of the function call node
      boolean hasMoreThanOneArgument = false;
      
      for (int i = 1; i < children.length; i++) {
        if (children[i] != value) { // Check if there's more than one child after the first argument
          hasMoreThanOneArgument = true;
          break;
        }
      }
      
      if (!hasMoreThanOneArgument) {
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