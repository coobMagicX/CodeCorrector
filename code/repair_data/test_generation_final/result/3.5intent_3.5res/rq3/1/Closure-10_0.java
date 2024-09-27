static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

static boolean mayBeStringHelper(Node n) {
  if (n == null) {
    return false;
  }
  if (n.isString()) {
    return true;
  }
  for (Node child : n.getChildren()) {
    if (mayBeStringHelper(child)) {
      return true;
    }
  }
  return false;
}