static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    // Validate input type and condition for recursive calls
    if (!(n instanceof Node)) {
      throw new RuntimeException("Input 'n' must be an instance of Node");
    }
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    // Investigate potential issues with the mayBeStringHelper method in handling edge cases
    // and add explicit type checks and error handling for unexpected node types
    if (!(n instanceof Node)) {
      throw new RuntimeException("Input 'n' must be an instance of Node");
    }
    return mayBeStringHelper(n);
  }
}