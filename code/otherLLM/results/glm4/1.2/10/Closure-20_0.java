private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Fold String(a) to '' + (a) on immutable literals,
    // which allows further optimizations
    Node value = callTarget.getNext();
    if (value != null) {
      // Check if the function call has additional arguments
      boolean hasAdditionalArgs = value.getNext() != null;
      
      if (!hasAdditionalArgs || value.getString().equals("new")) {
        // If there are no additional arguments or it's a new call, proceed with transformation
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