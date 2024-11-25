private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Fold simple function calls with 'String' constructor into their
    // equivalent string concatenation format for optimization.
    // This should only happen when there's a single argument, which is a simple literal.
    
    Node value = callTarget.getNext();
    if (value != null) {
      // Check if the next node is a simple literal to fold it into a string concatenation.
      if (value.isLiteral() && !value.isTypeOf(Node.Type.ARGUMENT)) {
        Node addition = IR.add(
            IR.string("").srcref(callTarget),
            value.detachFromParent());
        n.getParent().replaceChild(n, addition);
        reportCodeChange();
        return addition;
      }
    }
  }
  // If it's not a simple function call or the next node is not a simple literal,
  // do not fold and return the original node.
  return n;
}