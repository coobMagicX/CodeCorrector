static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, node -> mayBeString(node, false));
  } else {
    return mayBeStringHelper(n);
  }
}