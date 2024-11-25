private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of of the "this" context of a call.  A call without an
  // explicit "this" is a free call.
  Node first = n.getFirstChild();

  // ignore cast nodes and check for direct evaluation
  if (first != null && !NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
    
    // Additional checks for the 'eval' method
    if (first.isName() && "eval".equals(first.getString())) {
      Node second = first.getNextSibling();
      
      // Check if there is a pair of parentheses that indicate an explicit context
      if (second != null && NodeUtil.isParenthesis(second) &&
          second.getNextSibling().getNextSibling() != null && 
          NodeUtil.isCall(second.getNextSibling().getNextSibling())) {
        // If it's in the form "(0, eval)()", set as direct evaluation for the call node
        second.getNextSibling().putBooleanProp(Node.DIRECT_EVAL, true);
      }
    }
  }
}