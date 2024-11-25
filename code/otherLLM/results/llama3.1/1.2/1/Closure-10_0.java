static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    if (n == null || !isValidNode(n)) { // added input validation
      return false;
    }
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    if (n == null || !isValidNode(n)) { // added input validation
      return mayBeStringHelper(n) ? true : false; // corrected handling of non-recursive case
    }
    return mayBeStringHelper(n);
  }
}