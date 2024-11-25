private Node tryFoldSimpleFunctionCall(Node n) {
  Preconditions.checkState(n.isCall());
  Node callTarget = n.getFirstChild();
  
  // Generalize the logic to handle more cases beyond just String() calls
  if (callTarget != null && callTarget.isName()) {
    String functionName = callTarget.getString();
    
    // Check for specific function names that can be optimized
    if (functionName.equals("String") || functionName.equals("parseInt") || functionName.equals("parseFloat")) {
      Node value = callTarget.getNext();
      if (value != null) {
        Node addition = IR.add(
            IR.string("").srcref(callTarget),
            value.detachFromParent());
        n.getParent().replaceChild(n, addition);
        reportCodeChange();
        return addition;
      }
    }
  }
  
  // If the conditions above are not met, try to handle other types of nodes
  else if (n.isUnaryOperation()) {
    Node unaryOperator = n.getFirstChild();
    
    // Check for specific operator names that can be optimized
    if (unaryOperator != null && unaryOperator.getString().equals("-")) {
      Node operand = unaryOperator.getNext();
      if (operand != null) {
        Node negation = IR.unaryMinus(operand.detachFromParent());
        n.getParent().replaceChild(n, negation);
        reportCodeChange();
        return negation;
      }
    }
  }
  
  return n;
}