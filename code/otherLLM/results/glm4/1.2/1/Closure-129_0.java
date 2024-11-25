private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  // Keep track of the "this" context of a call. A call without an explicit "this"
  // is a free call. We need to check if the first child node has a "this" reference.
  Node first = n.getFirstChild();

  // Check for "this" in case of a method call
  boolean isThisCall = (first.isAccess() && "this".equals(first.getString()));
  
  if (!NodeUtil.isGet(first) && !isThisCall) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  // Keep track of the context in which eval is called. It is important to distinguish
  // between "(0, eval)()" and "eval()". If the first child node is a method name,
  // we check if it's an explicit call or a direct call.
  if (first.isName()) {
    String methodName = first.getString();
    boolean isEvalCall = "eval".equals(methodName);
    
    if (isEvalCall) {
      first.putBooleanProp(Node.DIRECT_EVAL, true);
    }
  }

  // If the call is a method call with "this" as the receiver, mark it accordingly.
  if (isThisCall) {
    first.putBooleanProp(Node.THIS_CALL, true);
  }
}