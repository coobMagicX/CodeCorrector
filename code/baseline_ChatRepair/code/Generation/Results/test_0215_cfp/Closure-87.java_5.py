private boolean isFoldableExpressBlock(Node n) {
  if (n.getType() == Token.BLOCK && n.hasOneChild()) {
    Node firstChild = n.getFirstChild();
    if (firstChild.getType() == Token.EXPR_RESULT) {
      Node grandChild = firstChild.getFirstChild();
      // Ensure encounter of a CALL expression
      if (grandChild.getType() == Token.CALL) {
        // Check the callee and specifically ensure it isn't a typical event-handler or such calls.
        Node callee = grandChild.getFirstChild();
        if (callee != null && callee.isGetProp() && callee.getLastChild().getString().startsWith("on")) {
          // This heuristically detects event-handler properties such as 'onclick', 'onchange', etc.,
          // and decides not to fold such blocks to avoid impacting event handling behavior in certain browsers.
          return false;
        } 
      }
    }
    // Default folding consideration for other cases as usual
    return true;
  }
  return false;
}
