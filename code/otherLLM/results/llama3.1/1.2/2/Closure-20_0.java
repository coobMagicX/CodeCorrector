private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  if (callTarget != null && callTarget.isName()) { // Expanded scope to consider any function calls
    String functionName = callTarget.getString();
    Node value = callTarget.getNext(); // Handle multiple arguments by getting the first argument
    if (value != null) {
      int numArgs = value.getChildCount(); // Count the number of arguments
      List<Node> args = new ArrayList<>();
      for (int i = 0; i < numArgs; i++) { // Create a list of arguments
        Node arg = value.getFirstChild();
        args.add(arg.detachFromParent());
      }
      if (functionName.equals("String") && numArgs == 1) { // Specific case for String() function call with one argument
        Node addition = IR.add(IR.string("").srcref(callTarget), args.get(0));
        n.getParent().replaceChild(n, addition);
        reportCodeChange();
        return addition;
      }
    }
  }
  return n;
}