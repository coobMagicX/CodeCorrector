static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    // Recursively check this node and all child nodes
    return mayBeStringRecursive(n);
  } else {
    // Only check this node
    return mayBeStringHelper(n);
  }
}

// Recursively checks if any child nodes may be a string
private static boolean mayBe