static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return anyResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

private static boolean anyResultsMatch(Node n, Predicate<Node> predicate) {
  // Check the current node
  if (predicate.test(n)) {
    return true;
  }
  
  // Recursively check each child node
  for (Node child = n.getFirstChild();