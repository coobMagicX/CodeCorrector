static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    // Changed mayBeStringHelper(n) to mayBeStringHelperRecursive(n)
    return mayBeStringHelperRecursive(n);
  }
}

// New helper method
private static boolean mayBeStringHelperRecursive(Node n) {
  if (n.getType() == Token.STRING) {
    return true;
  } else if (n.getType() == Token.NAME && "string".equals(n.getString())) {
    return true;
  } else {
    // Recursively check children
    for (Node child : n.getChildren()) {
      if (mayBeStringHelperRecursive(child)) {
        return true;
      }
    }
  }
  return false;
}