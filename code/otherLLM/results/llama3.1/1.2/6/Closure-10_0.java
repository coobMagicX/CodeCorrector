static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    TernaryValue ternaryResult = getImpureBooleanValue(n);
    return ternaryResult.equals(TernaryValue.TRUE)
        || allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    // Modified to correctly handle OR operators
    boolean result = mayBeStringHelper(n.getFirstChild());
    Node lastChild = n.getLastChild();
    if (lastChild.getType() == Token.OR) {
      result = result && mayBeString(lastChild.getFirstChild(), true)
          && mayBeString(lastChild.getLastChild(), true);
    } else {
      result = result && allResultsMatch(lastChild, MAY_BE_STRING_PREDICATE);
    }
    return result;
  }
}