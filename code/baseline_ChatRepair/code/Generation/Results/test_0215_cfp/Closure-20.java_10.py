private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
      callTarget.getString().equals("String")) {
    Node firstArg = callTarget.getNext();
    // Ensure there is exactly one argument to the String function
    if (firstArg != null && firstArg.getNext() == null) {
      // Apply transformation if the argument is a simple literal that doesn't need evaluation:
      // strings, numbers, booleans, null or undefined.
      if (isSimpleLiteral(firstArg)) {
        Node addition = IR.add(
            IR.string("").srcref(callTarget), 
            firstArg.detachFromParent());
        n.getParent().replaceChild(n, addition);
        reportCodeChange();
        return addition;
      }
    }
  }
  return n;
}

private boolean isSimpleLiteral(Node node) {
  return node.isString() || node.isNumber() || node.isBoolean() ||
         node.isNull() || node.isVoid();
}
