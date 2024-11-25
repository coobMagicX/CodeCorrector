private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Fold String(a, b) to '' + (a) + (b) on immutable literals,
    // which allows further optimizations
    //
    // We can't do this in the general case, because String(a, b) has
    // slightly different semantics than '' + (a) + (b). See
    // http://code.google.com/p/closure-compiler/issues/detail?id=759
    Node value = callTarget.getNext();
    if (value != null) {
      List<Node> args = new ArrayList<>();
      while (value != null && value.isCall()) {
        Node arg = value.getFirstChild(); // Should get the first argument
        if (arg != null) { // Ensure there is an argument to add
          args.add(arg);
        }
        value = value.getNext(); // Move to the next argument or end
      }

      Node addition = IR.string("").srcref(callTarget);
      for (Node arg : args) {
        addition = IR.add(addition, arg.detachFromParent());
      }

      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  return n;
}