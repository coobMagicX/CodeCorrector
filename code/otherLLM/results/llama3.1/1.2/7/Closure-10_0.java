static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    // Check non-string literals first
    if (isNumericResult(n) || isBooleanResult(n)) {
      return false;
    }
    
    return mayBeStringHelper(n);
  }
}