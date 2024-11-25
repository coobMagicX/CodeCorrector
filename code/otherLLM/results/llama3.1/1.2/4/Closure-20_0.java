private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && (callTarget.isName() &&
        callTarget.getString().equals("String") || // Fold String(a) to '' + (a)
        (callTarget.isCall() && callTarget.getString().equals("String")))) { // Handle complex arguments passed to String()
    Node value = callTarget.getFirstChild();
    if (value != null) {
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          value.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  // Improved handling of multiple child nodes
  else if (n.getChildrenSize() > 1) {
    Node firstArg = n.getFirstChild();
    Node secondArg = n.getChild(1);
    if (firstArg != null && secondArg != null) {
      Node addition = IR.add(
          firstArg.detachFromParent(),
          secondArg.detachFromParent());
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  return n;
}