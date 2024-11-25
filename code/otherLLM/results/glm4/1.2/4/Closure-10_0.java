static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    // Make sure to pass the correct predicate for checking strings.
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    // Use the existing method to determine if the node is not numeric, not boolean, undefined, or null
    // This should be correct as per the given method definition in the context.
    return mayBeStringHelper(n);
  }
}