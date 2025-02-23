private void annotateCalls(Node n) {
  Preconditions.checkState(n.isCall());

  
  
  Node first = n.getFirstChild();

  
  
  first = skipCastNodes(first);

  if (!NodeUtil.isGet(first)) {
    n.putBooleanProp(Node.FREE_CALL, true);
  }

  
  
  if (first.isName() && "eval".equals(first.getString())) {
    first.putBooleanProp(Node.DIRECT_EVAL, true);
  }
}

/**
 * Helper method to skip all cast nodes and return the significant child.
 *
 * @param node The first child node which might be cast.
 * @return The first non-c