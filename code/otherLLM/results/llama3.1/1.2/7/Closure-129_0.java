private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an explicit "this" is a free call.
  Node first = n.getFirstChild();

  // ignore cast nodes.
  
  if (!NodeUtil.isGet(first)) { // Check if first child is not a get node
    n.putBooleanProp(Node.FREE_CALL, true); // Set free_call prop to true
    
    // Check if the function call has an implicit "this" context
    if (first.isName() && 
        !first.getString().equals("eval") && // Ignore eval calls for now
        first.getPrevSibling() != null) { // Check if there's a previous sibling that could be the "this" context
      first.putBooleanProp(Node.IMPLICIT_THIS, true); // Set implicit_this prop to true
    }
  }

  // Keep track of the context in which eval is called. It is important 
  // to distinguish between "(0, eval)()" and "eval()".
  if (first.isName() && 
      "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}