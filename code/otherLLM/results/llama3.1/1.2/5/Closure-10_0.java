static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    // Validate conditional expressions within the folded string
    if (n instanceof IfExpr && ((IfExpr)n).getCondition() != null) {
      // Handle ternary operator and OR operator correctly in string literal evaluation
      return mayBeStringHelper(((IfExpr)n).getThen());
    }
    return mayBeStringHelper(n);
  }
}