static boolean mayBeString(Node n, boolean recurse) {
  if (recurse && n.hasChildren()) {
    return anyChildResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

private static boolean anyChildResultsMatch(Node n, Predicate<Node> predicate) {
  for