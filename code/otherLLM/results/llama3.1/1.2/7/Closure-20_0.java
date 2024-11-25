private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName() &&
        callTarget.getString().equals("String")) {
    // Fold String(a) to '' + (a) on immutable literals,
    // which allows further optimizations
    //
    // We can generalize the folding logic beyond just taking a single argument
    Node value = callTarget.getNext();
    if (value != null && value.isCall() || value.isLiteral()) {
      // Handle edge cases like passing an expression or another function call as the first argument to String()
      StringBuilder sb = new StringBuilder();
      while (true) {
        if (value.isName() && value.getString().equals("String")) {
          break;
        } else if (value.isLiteral() || value.isCall()) {
          sb.append(value.toString());
          // If we hit a function call or another String(), append its arguments
          if (value.isCall() && value.getFirstChild().isName() &&
                  value.getFirstChild().getString().equals("String")) {
            Node arg = value.getFirstChild().getNext();
            while (arg != null) {
              sb.append(arg.toString());
              arg = arg.getNext();
            }
          }
        } else {
          break;
        }
        if (!value.isNext()) break;
        value = value.getNext();
      }
      Node addition = IR.add(
          IR.string("").srcref(callTarget),
          IR.parse(sb.toString()));
      n.getParent().replaceChild(n, addition);
      reportCodeChange();
      return addition;
    }
  }
  return n;
}